import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 18621,
  auctionCategory: 'SCULPTURES',
  lotNumber: 8229,
  authorName: 'intently impure',
  producedYear: dayjs('2024-12-27T23:59'),
  classification: 'ABSTRACT',
  estimatedPrice: 11495.88,
};

export const sampleWithPartialData: IProduct = {
  id: 19921,
  auctionCategory: 'PAINTINGS',
  lotNumber: 12822,
  authorName: 'failing why yum',
  producedYear: dayjs('2024-12-27T17:54'),
  classification: 'SEASCAPE',
  estimatedPrice: 26832.81,
  auctionDate: dayjs('2024-12-27T19:17'),
};

export const sampleWithFullData: IProduct = {
  id: 6021,
  auctionCategory: 'CARVINGS',
  lotNumber: 25271,
  authorName: 'for oof',
  producedYear: dayjs('2024-12-27T12:45'),
  classification: 'ABSTRACT',
  estimatedPrice: 9533.54,
  description: 'quaver nor boohoo',
  auctionDate: dayjs('2024-12-27T18:06'),
  additionalInformation: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewProduct = {
  auctionCategory: 'PHOTOGRAPHIC_IMAGES',
  lotNumber: 18687,
  authorName: 'state primary',
  producedYear: dayjs('2024-12-28T03:55'),
  classification: 'PORTRAIT',
  estimatedPrice: 18098.75,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
