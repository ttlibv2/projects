import { Component, input, Input, ViewChild, ViewEncapsulation } from "@angular/core";
import { ImageModule } from "primeng/image";
import { OverlayPanelModule } from "primeng/overlaypanel";
import { AvatarModule } from "primeng/avatar";
import { PImageDirective } from "./comps/p-image";
import { CommonModule } from "@angular/common";
import { Menu, MenuModule } from "primeng/menu";
import { MenuItem } from "primeng/api";
import { AppendTo, DomHandler, INgStyle, MenuProps } from "ts-ui/common";
import { Objects } from "ts-ui/helper";

const defaultMenu = [
    { label: 'Thông tin tài khoản', icon: 'pi pi-id-card' },
    { label: 'Cài đặt và quyền riêng tư', icon: 'pi pi-cog' },
    { separator: true },
    { label: 'Điều khoản dịch vụ', icon: 'pi pi-receipt' },
    { label: 'Chính sách bảo mật', icon: 'pi pi-lock' },
    { separator: true },
    { label: 'Đăng xuất', icon: 'pi pi-sign-out' },
];

const defaultMenuOpt: MenuProps = {
    autoZIndex: true,
    baseZIndex: 999,
}

@Component({
    standalone: true,
    encapsulation: ViewEncapsulation.None,
    imports: [CommonModule, AvatarModule, OverlayPanelModule, MenuModule],
    selector: 'ts-app-profile',
    templateUrl: './view/profile.html'

})
export class AppProfile {
    @Input() image: string = '../assets/images/profile.png';
    @Input() icon: string;// = 'pi pi-user';
    @Input() name: string = 'Alen Miller';
    @Input() subName: string = 'UI Designer';
    @Input() items: MenuItem[] = defaultMenu;
    @Input() menuId: string;
    @Input() menuStyle: INgStyle;
    @Input() menuClass: string;

    @Input() set menuOption(opt: MenuProps) {
        this._menuOption = Objects.mergeDeep({ ...defaultMenuOpt }, opt);
    };

    get menuOption(): MenuProps {
        return this._menuOption;
    }

    showMenu(event: any): void {
        if (this.menuOption?.onShow) {
            this.menuOption.onShow();
        }
     }

    hideMenu(event: any): void { 
        if(this.menuOption?.onHide) {
            this.menuOption.onHide();
        }
    }

    private _menuOption: MenuProps = defaultMenuOpt;
}