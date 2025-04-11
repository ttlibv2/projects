import { Runner, RunnerFactory } from '@ngdev/devkit-core/runners';
import { AbstractCollection } from './abstract.collection';
import { Collection } from './collection';
import { CustomCollection } from './custom.collection';
import {NestCollection} from "./nestjs.collection";
import {NgDevCollection} from "./ngdev.collection";
import {AngularCollection} from "./angular.collection";

export abstract class CollectionFactory {

  static create(collection: Collection | string): AbstractCollection {
    const schematicRunner = RunnerFactory.create(Runner.SCHEMATIC);
    switch (collection) {
      case '@nestjs/schematics': return new NestCollection(schematicRunner);
      case '@ngdev/schematics': return new NgDevCollection(schematicRunner);
      case '@schematics/angular': return new AngularCollection(schematicRunner);
      default: return new CustomCollection(collection, schematicRunner);
    }
  }
}