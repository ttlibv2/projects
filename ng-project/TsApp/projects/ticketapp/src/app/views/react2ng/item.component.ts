import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { FormGroup, FormsBuilder, FormsModule } from 'ts-ui/forms';
import { CorsService } from '../../services/cors.service';
import { InputGroup } from './prefix';
import { MultiSelectModule } from 'primeng/multiselect';
import { DropdownModule } from 'primeng/dropdown';

const apiUrl: string = 'https://syntha.ai/api/ai-public/converter';

@Component({
  selector: 'ts-react-ng',
  standalone: true,
  encapsulation: ViewEncapsulation.None,
  templateUrl: './item.component.html',
  imports: [CommonModule, InputTextareaModule, ButtonModule, FormsModule,InputGroup, DropdownModule],
  providers: [
  ]
})
export class Rect2NgComponent implements OnInit {
  form: FormGroup;
  hasSend: boolean = false;

  get cTarget(): FormControl {
    return this.form?.get('target') as any;
  }

  constructor(private fb: FormsBuilder,
    private cors: CorsService) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      source: [null, Validators.required],
      target: [null]
    });
  }

  handleConvert(): void {
    const { source } = this.form.getRawValue();
    this.hasSend = true;

    this.cors.react2Angular(source).subscribe({
      error: msg => {
        this.cTarget.setValue(JSON.stringify(msg));
        this.hasSend = false;
      },
      next: (res: string) => {
        console.log(res)
        this.cTarget.setValue(res);
        this.hasSend = false;
      }



    });
  }
}
