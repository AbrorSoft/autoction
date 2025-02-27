import dayjs from 'dayjs/esm';
import { AuctionCategory } from 'app/entities/enumerations/auction-category.model';
import { Classification } from 'app/entities/enumerations/classification.model';

export interface IProduct {
  id: number;
  auctionCategory?: keyof typeof AuctionCategory | null;
  lotNumber?: number | null;
  authorName?: string | null;
  imageKey?: string | null;
  imageFile?: BinaryData | null;
  producedYear?: dayjs.Dayjs | null;
  classification?: keyof typeof Classification | null;
  estimatedPrice?: number | null;
  description?: string | null;
  auctionDate?: dayjs.Dayjs | null;
  additionalInformation?: any | null;
  type?: string | null;
  imageType?: string | null;
  isFramed?: boolean | null;
  height?: number | null;
  length?: number | null;
  width?: number | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
