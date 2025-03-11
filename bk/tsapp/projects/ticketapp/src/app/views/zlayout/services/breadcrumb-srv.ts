import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, NavigationEnd, Router } from "@angular/router";
import { MenuItem } from "primeng/api";
import { BehaviorSubject, filter, Observable } from "rxjs";
import { Objects } from "ts-ui/helper";

const { notNull } = Objects;

export interface BreadcrumbItem extends MenuItem {}

@Injectable({providedIn: 'root'})
export class BreadcrumbService {
    private readonly _breadcrumbs$ = new BehaviorSubject<BreadcrumbItem[]>([]);

    get breadcrumbs$(): Observable<BreadcrumbItem[]> {
        return this._breadcrumbs$.asObservable();
    }

    constructor(private router: Router) {
        this.initializeRouter(router);
    }

    private initializeRouter(router: Router): void {
        router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe(_ => {
                // Construct the breadcrumb hierarchy
                const root = this.router.routerState.snapshot.root;
                const breadcrumbs: BreadcrumbItem[] = [];
                this.addBreadcrumb(root, [], breadcrumbs);

                // Emit the new hierarchy
                this._breadcrumbs$.next(breadcrumbs);
            });

    }

    private addBreadcrumb(route: ActivatedRouteSnapshot, parentUrl: string[], breadcrumbs: BreadcrumbItem[]) {        

        if (route) {
            // Construct the route URL
            const routeUrl = parentUrl.concat(route.url.map(url => url.path));

            // Add an element for the current route part
            if ('breadcrumb' in route.data && route.url.length ) {                
                breadcrumbs.push(this.prepareBreadcrumbItem(route, route.data, routeUrl));
            }

            // Add another element for the next route part
            this.addBreadcrumb(route.firstChild, routeUrl, breadcrumbs);
        }
    }

    private prepareBreadcrumbItem(route: ActivatedRouteSnapshot, item: any, routeUrl: string[]): BreadcrumbItem {
        const { breadcrumb: bc } = item;
        const funcLabel = (label: any) => label === 'function' ? label(item) : label;
        const bcItem: BreadcrumbItem = typeof bc === 'object' ? bc: {label: bc};
        return {
            ...bcItem, routerData: item,
            label: funcLabel(bcItem['label']),
            routerLink: !!route.component ? `/${routeUrl.join('/')}` : undefined
        }
        
    }


}