import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProduct, NewProduct } from '../product.model';
import { AuctionCategory } from '../../enumerations/auction-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProduct | NewProduct> = Omit<T, 'producedYear' | 'auctionDate'> & {
  producedYear?: string | null;
  auctionDate?: string | null;
};

type ProductFormRawValue = FormValueOf<IProduct>;

type NewProductFormRawValue = FormValueOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id' | 'producedYear' | 'auctionDate' | 'lotNumber'>;

type ProductFormGroupContent = {
  id: FormControl<ProductFormRawValue['id'] | NewProduct['id']>;
  auctionCategory: FormControl<ProductFormRawValue['auctionCategory']>;
  lotNumber: FormControl<ProductFormRawValue['lotNumber']>;
  imageKey: FormControl<ProductFormRawValue['imageKey']>;
  imageFile: FormControl<ProductFormRawValue['imageFile']>;
  authorName: FormControl<ProductFormRawValue['authorName']>;
  producedYear: FormControl<ProductFormRawValue['producedYear']>;
  classification: FormControl<ProductFormRawValue['classification']>;
  estimatedPrice: FormControl<ProductFormRawValue['estimatedPrice']>;
  description: FormControl<ProductFormRawValue['description']>;
  auctionDate: FormControl<ProductFormRawValue['auctionDate']>;
  additionalInformation: FormControl<ProductFormRawValue['additionalInformation']>;
  type: FormControl<ProductFormRawValue['type']>;
  length: FormControl<ProductFormRawValue['length']>;
  height: FormControl<ProductFormRawValue['height']>;
  isFramed: FormControl<ProductFormRawValue['isFramed']>;
  width: FormControl<ProductFormRawValue['width']>;
  imageType: FormControl<ProductFormRawValue['imageType']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = this.convertProductToProductRawValue({
      ...this.getFormDefaults(),
      ...product,
    });
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      auctionCategory: new FormControl(productRawValue.auctionCategory, {
        validators: [Validators.required],
      }),
      lotNumber: new FormControl(productRawValue.lotNumber, {
        validators: [Validators.required],
      }),
      imageKey: new FormControl(productRawValue.imageKey, {
        validators: [Validators.required],
      }),
      imageFile: new FormControl(productRawValue.imageFile, {
        validators: [Validators.required],
      }),
      authorName: new FormControl(productRawValue.authorName, {
        validators: [Validators.required],
      }),
      producedYear: new FormControl(productRawValue.producedYear, {
        validators: [Validators.required],
      }),
      classification: new FormControl(productRawValue.classification, {
        validators: [Validators.required],
      }),
      estimatedPrice: new FormControl(productRawValue.estimatedPrice, {
        validators: [Validators.required],
      }),
      description: new FormControl(productRawValue.description),
      auctionDate: new FormControl(productRawValue.auctionDate),
      additionalInformation: new FormControl(productRawValue.additionalInformation),
      length: new FormControl(productRawValue.length),
      height: new FormControl(productRawValue.height),
      type: new FormControl(productRawValue.type),
      width: new FormControl(productRawValue.width),
      imageType: new FormControl(productRawValue.imageType),
      isFramed: new FormControl(productRawValue.isFramed ?? false),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return this.convertProductRawValueToProduct(form.getRawValue() as ProductFormRawValue | NewProductFormRawValue);
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = this.convertProductToProductRawValue({ ...this.getFormDefaults(), ...product });
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    const currentTime = dayjs();
    let date: any = new Date().getTime() + '';
    date.slice(4, -1);
    return {
      id: null,
      producedYear: currentTime,
      lotNumber: date,
    };
  }

  private convertProductRawValueToProduct(rawProduct: ProductFormRawValue | NewProductFormRawValue): IProduct | NewProduct {
    return {
      ...rawProduct,
      producedYear: dayjs(rawProduct.producedYear, DATE_TIME_FORMAT),
      auctionDate: dayjs(rawProduct.auctionDate, DATE_TIME_FORMAT),
    };
  }
  private convertProductToProductRawValue(
    product: IProduct | (Partial<NewProduct> & ProductFormDefaults),
  ): ProductFormRawValue | PartialWithRequiredKeyOf<NewProductFormRawValue> {
    return {
      ...product,
      producedYear: product.producedYear ? product.producedYear.format(DATE_TIME_FORMAT) : undefined,
      auctionDate: product.auctionDate ? product.auctionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
