import {ComponentRef, Injectable} from '@angular/core';
import {Observable} from "rxjs";

export interface ActiveToast<C> {
  /** Your Toast ID. Use this to close it individually */
  toastId: number;
  /** the title of your toast. Stored to prevent duplicates */
  title: string;
  /** the message of your toast. Stored to prevent duplicates */
  message: string;
  /** a reference to the component see portal.ts */
  portal: ComponentRef<C>;
  /** a reference to your toast */
  toastRef: ToastRef<C>;
  /** triggered when toast is active */
  onShown: Observable<void>;
  /** triggered when toast is destroyed */
  onHidden: Observable<void>;
  /** triggered on toast click */
  onTap: Observable<void>;
  /** available for your use in custom toast */
  onAction: Observable<any>;
}

@Injectable({  providedIn: 'root'})
export class ToastService {

  constructor() { }
}
