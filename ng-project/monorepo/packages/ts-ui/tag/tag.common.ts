export type TagMode = 'closeable' | 'checkable' | 'default';

export interface TagRemoveEvent {
  event: MouseEvent;
  value: any;
}

export interface ImageErrorEvent {
  event: Event;
  image: string;
}