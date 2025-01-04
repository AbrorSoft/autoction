import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'eab2dd9d-7cef-4c22-9ac1-e5d3cd9e7f0e',
};

export const sampleWithPartialData: IAuthority = {
  name: '6d7a1f23-f581-4777-807c-896b853fdb72',
};

export const sampleWithFullData: IAuthority = {
  name: '6935d2a0-1780-4146-9547-a944880b09da',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
