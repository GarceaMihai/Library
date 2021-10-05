import {Rental} from './rental';
import {Book} from './book';

export class User {
  id: number;
  username: string;
  email: string;
  password: string;
  role: string;
  // rentals?: Rental[] = <Rental[]>{};
  // bookWishlist?: Book[] = <Book[]>{};
}
