/*
 * Copyright 2009 - 2016 Denys Pavlov, Igor Azarnyi
 *
 *    Licensed under the Apache License, Version 2.0 (the 'License');
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an 'AS IS' BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ShopEventBus, PricingService, UserEventBus, Util } from './../shared/services/index';
import { PromotionTestConfigComponent } from './components/index';
import { ModalComponent, ModalResult, ModalAction } from './../shared/modal/index';
import { PriceListVO, ShopVO, PromotionTestVO, CartVO } from './../shared/model/index';
import { FormValidationEvent, Futures, Future } from './../shared/event/index';
import { Config } from './../shared/config/env.config';
import { UiUtil } from './../shared/ui/index';
import { LogUtil } from './../shared/log/index';
import { CookieUtil } from './../shared/cookies/index';

@Component({
  selector: 'yc-shop-pricelist',
  moduleId: module.id,
  templateUrl: 'shop-pricelist.component.html',
})

export class ShopPriceListComponent implements OnInit, OnDestroy {

  private static COOKIE_SHOP:string = 'ADM_UI_PRICE_SHOP';
  private static COOKIE_CURRENCY:string = 'ADM_UI_PRICE_CURR';

  private static _selectedShop:ShopVO;
  private static _selectedCurrency:string;

  private static PRICELIST:string = 'pricelist';
  private static PRICE:string = 'price';
  private static PRICELIST_TEST:string = 'pricelisttest';

  private searchHelpShow:boolean = false;
  private forceShowAll:boolean = false;
  private viewMode:string = ShopPriceListComponent.PRICELIST;

  private pricelist:Array<PriceListVO> = [];
  private pricelistFilter:string;
  private pricelistFilterRequired:boolean = true;
  private pricelistFilterCapped:boolean = false;

  private delayedFiltering:Future;
  private delayedFilteringMs:number = Config.UI_INPUT_DELAY;
  private filterCap:number = Config.UI_FILTER_CAP;
  private filterNoCap:number = Config.UI_FILTER_NO_CAP;

  private selectedPricelist:PriceListVO;

  private pricelistEdit:PriceListVO;

  @ViewChild('deleteConfirmationModalDialog')
  private deleteConfirmationModalDialog:ModalComponent;

  @ViewChild('selectShopModalDialog')
  private selectShopModalDialog:ModalComponent;

  @ViewChild('selectCurrencyModalDialog')
  private selectCurrencyModalDialog:ModalComponent;

  @ViewChild('runTestModalDialog')
  private runTestModalDialog:PromotionTestConfigComponent;

  private deleteValue:String;

  private selectedSkuCode:String;

  private loading:boolean = false;

  private testCart:CartVO;

  private changed:boolean = false;
  private validForSave:boolean = false;

  private userSub:any;

  constructor(private _priceService:PricingService) {
    LogUtil.debug('ShopPriceListComponent constructed');
  }

  get selectedShop():ShopVO {
     return ShopPriceListComponent._selectedShop;
  }

  set selectedShop(selectedShop:ShopVO) {
    ShopPriceListComponent._selectedShop = selectedShop;
  }

  get selectedCurrency():string {
     return ShopPriceListComponent._selectedCurrency;
  }

  set selectedCurrency(selectedCurrency:string) {
    ShopPriceListComponent._selectedCurrency = selectedCurrency;
  }

  newPricelistInstance():PriceListVO {
    return {
      skuPriceId: 0,
      regularPrice: 0, minimalPrice: undefined, salePrice: undefined,
      salefrom: null, saleto:null,
      priceUponRequest: false,
      priceOnOffer: false,
      quantity: 1,
      currency: this.selectedCurrency,
      skuCode: '', skuName: '',
      shopCode: this.selectedShop.code,
      tag: null, pricingPolicy: null, supplier: null, ref: null,
      autoGenerated: false
    };
  }

  ngOnInit() {
    LogUtil.debug('ShopPriceListComponent ngOnInit');

    this.onRefreshHandler();

    this.userSub = UserEventBus.getUserEventBus().userUpdated$.subscribe(user => {
      this.presetFromCookie();
    });

    let that = this;
    this.delayedFiltering = Futures.perpetual(function() {
      that.getFilteredPricelist();
    }, this.delayedFilteringMs);

  }

  ngOnDestroy() {
    LogUtil.debug('ShopPriceListComponent ngOnDestroy');
    if (this.userSub) {
      this.userSub.unsubscribe();
    }
  }

  protected presetFromCookie() {

    if (this.selectedShop == null) {
      let shopCode = CookieUtil.readCookie(ShopPriceListComponent.COOKIE_SHOP, null);
      if (shopCode != null) {
        let shops = ShopEventBus.getShopEventBus().currentAll();
        if (shops != null) {
          shops.forEach(shop => {
            if (shop.code == shopCode) {
              this.selectedShop = shop;
              LogUtil.debug('ShopPriceListComponent ngOnInit presetting shop from cookie', shop);
            }
          });
        }
      }
    }
    if (this.selectedCurrency == null) {
      let curr = CookieUtil.readCookie(ShopPriceListComponent.COOKIE_CURRENCY, null);
      if (curr != null) {
        this.selectedCurrency = curr;
        LogUtil.debug('ShopPriceListComponent ngOnInit presetting currency from cookie', curr);
      }
    }

  }


  protected onShopSelect() {
    LogUtil.debug('ShopPriceListComponent onShopSelect');
    this.selectShopModalDialog.show();
  }

  protected onShopSelected(event:ShopVO) {
    LogUtil.debug('ShopPriceListComponent onShopSelected');
    this.selectedShop = event;
    if (this.selectedShop != null) {
      CookieUtil.createCookie(ShopPriceListComponent.COOKIE_SHOP, this.selectedShop.code, 360);
    }
  }

  protected onSelectShopResult(modalresult: ModalResult) {
    LogUtil.debug('ShopPriceListComponent onSelectShopResult modal result is ', modalresult);
    if (this.selectedShop == null) {
      this.selectShopModalDialog.show();
    } else if (this.selectedCurrency == null) {
      this.selectCurrencyModalDialog.show();
    } else {
      this.getFilteredPricelist();
    }
  }

  protected onCurrencySelect() {
    LogUtil.debug('ShopPriceListComponent onCurrencySelect');
    this.selectCurrencyModalDialog.show();
  }

  protected onCurrencySelected(event:string) {
    LogUtil.debug('ShopPriceListComponent onCurrencySelected');
    this.selectedCurrency = event;
    if (this.selectedCurrency != null) {
      CookieUtil.createCookie(ShopPriceListComponent.COOKIE_CURRENCY, this.selectedCurrency, 360);
    }
  }

  protected onSelectCurrencyResult(modalresult: ModalResult) {
    LogUtil.debug('ShopPriceListComponent onSelectCurrencyResult modal result is ', modalresult);
    if (this.selectedCurrency == null) {
      this.selectCurrencyModalDialog.show();
    } else {
      this.getFilteredPricelist();
    }
  }

  protected onTestRules() {
    LogUtil.debug('ShopPriceListComponent onTestRules');
    this.runTestModalDialog.showDialog();
  }

  onRunTestResult(event:PromotionTestVO) {
    LogUtil.debug('ShopPriceListComponent onRunTestResult', event);
    if (event != null) {
      this.loading = true;
      let _sub:any = this._priceService.testPromotions(this.selectedShop, this.selectedCurrency, event).subscribe(
        cart => {
          _sub.unsubscribe();
          this.loading = false;
          LogUtil.debug('ShopPriceListComponent onTestRules', cart);
          this.viewMode = ShopPriceListComponent.PRICELIST_TEST;
          this.testCart = cart;
        }
      );

    }
  }


  protected onFilterChange(event:any) {

    this.delayedFiltering.delay();

  }

  protected onRefreshHandler() {
    LogUtil.debug('ShopPriceListComponent refresh handler');
    if (UserEventBus.getUserEventBus().current() != null) {
      this.presetFromCookie();
      this.getFilteredPricelist();
    }
  }

  protected onPricelistSelected(data:PriceListVO) {
    LogUtil.debug('ShopPriceListComponent onPricelistSelected', data);
    this.selectedPricelist = data;
    this.selectedSkuCode = this.selectedPricelist ? this.selectedPricelist.skuCode : null;
  }

  protected onPriceChanged(event:FormValidationEvent<PriceListVO>) {
    LogUtil.debug('ShopPriceListComponent onPriceChanged', event);
    this.changed = true;
    this.validForSave = event.valid;
    this.pricelistEdit = event.source;
  }

  protected onSearchTag() {
    this.pricelistFilter = '#';
    this.searchHelpShow = false;
  }

  protected onSearchShipping() {
    this.pricelistFilter = '#shipping';
    this.searchHelpShow = false;
    this.getFilteredPricelist();
  }

  protected onSearchDate() {
    this.pricelistFilter = UiUtil.exampleDateSearch();
    this.searchHelpShow = false;
  }

  protected onSearchExact() {
    this.pricelistFilter = '!';
    this.searchHelpShow = false;
  }

  protected onForceShowAll() {
    this.forceShowAll = !this.forceShowAll;
    this.getFilteredPricelist();
  }

  protected onSearchHelpToggle() {
    this.searchHelpShow = !this.searchHelpShow;
  }

  protected onBackToList() {
    LogUtil.debug('ShopPriceListComponent onBackToList handler');
    if (this.viewMode === ShopPriceListComponent.PRICELIST_TEST ||
      this.viewMode === ShopPriceListComponent.PRICE) {
      this.viewMode = ShopPriceListComponent.PRICELIST;
    }
  }

  protected onRowNew() {
    LogUtil.debug('ShopPriceListComponent onRowNew handler');
    this.changed = false;
    this.validForSave = false;
    if (this.viewMode === ShopPriceListComponent.PRICELIST) {
      this.pricelistEdit = this.newPricelistInstance();
      this.viewMode = ShopPriceListComponent.PRICE;
    }
    this.selectedPricelist = null;
  }

  protected onRowDelete(row:PriceListVO) {
    LogUtil.debug('ShopPriceListComponent onRowDelete handler', row);
    this.deleteValue = row.skuCode;
    this.deleteConfirmationModalDialog.show();
  }

  protected onRowDeleteSelected() {
    if (this.selectedPricelist != null) {
      this.onRowDelete(this.selectedPricelist);
    }
  }


  protected onRowEditPricelist(row:PriceListVO) {
    LogUtil.debug('ShopPriceListComponent onRowEditPricelist handler', row);
    this.pricelistEdit = Util.clone(row);
    this.changed = false;
    this.validForSave = false;
    this.viewMode = ShopPriceListComponent.PRICE;
  }

  protected onRowEditSelected() {
    if (this.selectedPricelist != null) {
      this.onRowEditPricelist(this.selectedPricelist);
    }
  }

  protected onRowCopySelected() {
    if (this.selectedPricelist != null) {
      let copy:PriceListVO = Util.clone(this.selectedPricelist);
      copy.skuPriceId = 0;
      this.onRowEditPricelist(copy);
    }
  }

  protected onSaveHandler() {

    if (this.validForSave && this.changed) {

      if (this.pricelistEdit != null) {

        LogUtil.debug('ShopPriceListComponent Save handler pricelist', this.pricelistEdit);

        this.loading = true;
        let _sub:any = this._priceService.savePriceList(this.pricelistEdit).subscribe(
            rez => {
              _sub.unsubscribe();
              let pk = this.pricelistEdit.skuPriceId;
              LogUtil.debug('ShopPriceListComponent pricelist changed', rez);
              this.selectedPricelist = rez;
              this.validForSave = false;
              this.pricelistEdit = null;
              this.loading = false;
              this.viewMode = ShopPriceListComponent.PRICELIST;

              if (pk == 0) {
                this.pricelistFilter = '!' + rez.skuCode;
              }
              this.getFilteredPricelist();
          }
        );
      }

    }

  }

  protected onDiscardEventHandler() {
    LogUtil.debug('ShopPriceListComponent discard handler');
    if (this.viewMode == ShopPriceListComponent.PRICE) {
      if (this.selectedPricelist != null) {
        this.onRowEditSelected();
      } else {
        this.onRowNew();
      }
    }
  }

  protected onDeleteConfirmationResult(modalresult: ModalResult) {
    LogUtil.debug('ShopPriceListComponent onDeleteConfirmationResult modal result is ', modalresult);
    if (ModalAction.POSITIVE === modalresult.action) {

      if (this.selectedPricelist != null) {
        LogUtil.debug('ShopPriceListComponent onDeleteConfirmationResult', this.selectedPricelist);

        this.loading = true;
        let _sub:any = this._priceService.removePriceList(this.selectedPricelist).subscribe(res => {
          _sub.unsubscribe();
          LogUtil.debug('ShopPriceListComponent removePricelist', this.selectedPricelist);
          this.selectedPricelist = null;
          this.pricelistEdit = null;
          this.loading = false;
          this.getFilteredPricelist();
        });
      }
    }
  }

  protected onClearFilter() {

    this.pricelistFilter = '';
    this.getFilteredPricelist();

  }

  private getFilteredPricelist() {
    this.pricelistFilterRequired = !this.forceShowAll && (this.pricelistFilter == null || this.pricelistFilter.length < 2);

    LogUtil.debug('ShopPriceListComponent getFilteredPricelist' + (this.forceShowAll ? ' forcefully': ''));

    if (this.selectedShop != null && !this.pricelistFilterRequired) {
      this.loading = true;
      let max = this.forceShowAll ? this.filterNoCap : this.filterCap;
      let _sub:any = this._priceService.getFilteredPriceLists(this.selectedShop, this.selectedCurrency, this.pricelistFilter, max).subscribe( allpricelist => {
        LogUtil.debug('ShopPriceListComponent getFilteredPricelist', allpricelist);
        this.pricelist = allpricelist;
        this.selectedPricelist = null;
        this.pricelistEdit = null;
        this.viewMode = ShopPriceListComponent.PRICELIST;
        this.changed = false;
        this.validForSave = false;
        this.pricelistFilterCapped = this.pricelist.length >= max;
        this.loading = false;
        _sub.unsubscribe();
      });
    } else {
      this.pricelist = [];
      this.selectedPricelist = null;
      this.pricelistEdit = null;
      this.viewMode = ShopPriceListComponent.PRICELIST;
      this.changed = false;
      this.validForSave = false;
      this.pricelistFilterCapped = false;
    }
  }

}
