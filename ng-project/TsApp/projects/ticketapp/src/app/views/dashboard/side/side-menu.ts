import { CommonModule } from "@angular/common";
import { ChangeDetectionStrategy, Component, Input, ViewEncapsulation } from "@angular/core";
import { SideItemView } from "./side-item";
import { SideItem } from "./side.interface";
import { CdkScrollable } from "@angular/cdk/scrolling";

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    changeDetection: ChangeDetectionStrategy.OnPush,
    imports: [CommonModule, SideItemView],
    styles: [`ts-side-menu {display: block}`],
    styleUrls: ['./view/side-menu.scss'],
    selector: 'ts-side-menu',
    templateUrl: './view/side-menu.html'
})
export class SideMenu extends CdkScrollable {
    @Input() items: SideItem[] = [
        {
            title: "General", main: true
        }, {
            title: "Dashboard",
            icon: "pi pi-home",
            items: [{
                routerLink: "/dashboard/default",
                title: "Default",
            }, {
                routerLink: "/dashboard/ecommerce",
                title: "Ecommerce",
            }]
        }, {
            title: "Widgets",
            icon: "pi pi-microsoft",
            ////type: "sub",
            //active: !1,
            items: [{
                routerLink: "/widgets/general",
                title: "General",
                //type: "link"
            }, {
                routerLink: "/widgets/chart",
                title: "Chart",
                //type: "link"
            }]
        }, {
            //level: 1,
            title: "Page Layout",
            icon: "pi pi-table",
            ////type: "sub",
            //active: !1,
            items: [{
                routerLink: "/page-layout/hide-nav-scroll",
                title: "Hide Nav Scroll",
                //type: "link"
            }, {
                routerLink: "/page-layout/footer-dark",
                title: "Footer Dark",
                //type: "link"
            }, {
                routerLink: "/page-layout/footer-light",
                title: "Footer Light",
                //type: "link"
            }, {
                routerLink: "/page-layout/footer-fixed",
                title: "Footer Fixed",
                //type: "link"
            }]
        }, {
            title: "Applications", main: true
        }, {
            //level: 1,
            title: "Project",
            icon: "pi pi-folder",
            ////type: "sub",
            //active: !1,
            items: [{
                routerLink: "/project/list",
                title: "Project List",
                //type: "link"
            }, {
                routerLink: "/project/create",
                title: "Create New",
                //type: "link"
            }]
        }, {
            //level: 1,
            routerLink: "/file-manager",
            title: "File Manager",
            icon: "pi pi-file",
            //bookmark: !0,
            //type: "link"
        }, {
            //level: 1,
            title: "Ecommerce",
            ////type: "sub",
            icon: "pi pi-paypal",
            //active: !1,
            items: [{
                routerLink: "/ecommerce/add-products",
                title: "Add Product",
                //type: "link"
            }, {
                routerLink: "/ecommerce/products",
                title: "Product",
                //type: "link"
            }, {
                routerLink: "/ecommerce/product-page",
                title: "Product page",
                //type: "link"
            }, {
                routerLink: "/ecommerce/product-list",
                title: "Product list",
                //type: "link"
            }, {
                routerLink: "/ecommerce/payment-detail",
                title: "Payment Details",
                //type: "link"
            }, {
                routerLink: "/ecommerce/order-history",
                title: "Order History",
                //type: "link"
            }, {
                //level: 2,
                title: "Invoice",
                ////type: "sub",
                //active: !1,
                items: [{
                    routerLink: "/invoice/invoice-1",
                    title: "Invoice-1",
                    //type: "link"
                }, {
                    routerLink: "/invoice/invoice-2",
                    title: "Invoice-2",
                    //type: "link"
                }, {
                    routerLink: "/invoice/invoice-3",
                    title: "Invoice-3",
                    //type: "link"
                }, {
                    routerLink: "/invoice/invoice-4",
                    title: "Invoice-4",
                    //type: "link"
                }, {
                    routerLink: "/invoice/invoice-5",
                    title: "Invoice-5",
                    //type: "link"
                }, {
                    routerLink: "/invoice/invoice-6",
                    title: "Invoice-6",
                    //type: "link"
                }]
            }, {
                routerLink: "/ecommerce/cart",
                title: "Cart",
                //type: "link"
            }, {
                routerLink: "/ecommerce/wishlist",
                title: "Wishlist",
                //type: "link"
            }, {
                routerLink: "/ecommerce/checkout",
                title: "Checkout",
                //type: "link"
            }, {
                routerLink: "/ecommerce/pricing",
                title: "Pricing",
                //type: "link"
            }]
        }, {
            //level: 1,
            routerLink: "/letter-box",
            title: "Letter Box",
            icon: "pi pi-envelope",
            //type: "link"
        }, {
            //level: 1,
            title: "Chat",
            ////type: "sub",
            icon: "pi pi-comment",
            //active: !1,
            items: [{
                routerLink: "/chat/private-chat",
                title: "Private Chat",
                //type: "link"
            }, {
                routerLink: "/chat/group-chat",
                title: "Group Chat",
                //type: "link"
            }]
        }, {
            //level: 1,
            title: "Users",
            icon: "pi pi-user",
            ////type: "sub",
            //active: !1,
            items: [{
                routerLink: "/user/users-profile",
                title: "Users Profile",
                //type: "link"
            }, {
                routerLink: "/user/edit-profile",
                title: "Users Edit",
                //type: "link"
            }, {
                routerLink: "/user/users-cards",
                title: "Users Cards",
                //type: "link"
            }]
        }, {
            //level: 1,
            routerLink: "/bookmarks",
            title: "Bookmarks",
            icon: "pi pi-bookmark",
            //type: "link"
        }, {
            //level: 1,
            routerLink: "/contacts",
            title: "Contact",
            icon: "pi pi-address-book",
            //type: "link",
            //bookmark: !0
        }, {
            //level: 1,
            routerLink: "/tasks",
            title: "Tasks",
            icon: "pi pi-list-check",
            //type: "link"
        }, {
            //level: 1,
            routerLink: "/calender",
            title: "Calender",
            icon: "pi pi-calendar",
            //type: "link"
        }, {
            //level: 1,
            routerLink: "/social-app",
            title: "Social App",
            icon: "pi pi-share-alt",
            //bookmark: !0,
            //type: "link"
        }, {
            //level: 1,
            routerLink: "/todo",
            title: "To-Do",
            icon: "pi pi-list",
            //type: "link"
        }, {
            //level: 1,
            routerLink: "/search-result",
            title: "Search Result",
            icon: "pi pi-search",
            //type: "link"
        },
    ];
}