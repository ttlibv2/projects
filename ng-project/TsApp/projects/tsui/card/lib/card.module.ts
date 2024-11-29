import { NgModule } from "@angular/core";
import { CardSection } from "./card-section";
import { Card } from "./card";

@NgModule({
    imports: [CardSection, Card],
    exports: [CardSection, Card]
})
export class CradModule {}