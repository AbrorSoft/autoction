import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { AuctionCategory } from 'app/entities/enumerations/auction-category.model';
import { Classification } from 'app/entities/enumerations/classification.model';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import { ProductFormGroup, ProductFormService } from './product-form.service';
import {
  Carvings,
  Drawings,
  ImageType,
  PaintCategory,
  PhotographicImages,
  Sculptures,
} from '../../enumerations/additional-information-category.model';

@Component({
  standalone: true,
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;
  auctionCategoryValues = Object.keys(AuctionCategory);
  classificationValues = Object.keys(Classification);
  additionalInfoDrawings = Object.keys(Drawings);
  additionalInfoPaintCategory = Object.keys(PaintCategory);
  additionalInfoPhotographicImages = Object.keys(PhotographicImages);
  additionalInfoSculptures = Object.keys(Sculptures);
  additionalInfoCarvings = Object.keys(Carvings);
  imageType = Object.keys(ImageType);
  additionalInformation: any;
  length: number;
  height: number;
  isFramed: boolean = false;
  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected productService = inject(ProductService);
  protected productFormService = inject(ProductFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
        this.additionalInformation = this.editForm.get('auctionCategory')?.value;
      }
    });
  }
  onChange(): void {
    this.additionalInformation = this.editForm.get('auctionCategory')?.value;
  }
  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: any): void {
    console.log(event.target?.files![0]);
    this.dataUtils.loadFileToForm(event, this.editForm, 'imageFile', true).subscribe({
      next: () => this.editForm.patchValue({ imageKey: event.target?.files![0].name }),
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('auctionApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    let additionalInfo: any;
    let product = this.productFormService.getProduct(this.editForm);
    switch (product.auctionCategory) {
      case AuctionCategory.DRAWINGS:
        additionalInfo = {
          type: product.type,
          length: product.length,
          height: product.height,
          isFramed: product.isFramed,
        };
        break;

      case AuctionCategory.CARVINGS:
        additionalInfo = {
          type: product.type,
          length: product.length,
          height: product.height,
          width: product.width,
        };
        break;

      case AuctionCategory.PHOTOGRAPHIC_IMAGES:
        additionalInfo = {
          type: product.type,
          length: product.length,
          height: product.height,
          imageType: product.imageType,
        };
        break;
      case AuctionCategory.SCULPTURES:
        additionalInfo = {
          type: product.type,
          length: product.length,
          height: product.height,
          width: product.width,
        };
        break;
      case AuctionCategory.PAINTINGS:
        additionalInfo = {
          type: product.type,
          length: product.length,
          height: product.height,
          isFramed: product.isFramed,
        };
        break;
      default:
        additionalInfo = {};
    }
    product = { ...product, additionalInformation: additionalInfo };
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);
  }
  protected readonly AuctionCategory = AuctionCategory;
}
