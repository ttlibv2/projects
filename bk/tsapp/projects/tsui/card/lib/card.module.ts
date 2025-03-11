import { NgModule } from "@angular/core";
import { CardSection } from "./card-section";
import { Card } from "./card-view";

@NgModule({
    imports: [CardSection, Card],
    exports: [CardSection, Card]
})
export class CardModule {}