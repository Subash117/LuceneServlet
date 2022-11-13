import Controller from '@ember/controller';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';

export default class SignupController extends Controller {
  @tracked pass1;
  @tracked pass2;
  @tracked warn = '';
  @tracked disabled = 'hidden';

  @action
  checkPass(event) {
    this.pass2 = event.target.value;
    if (this.pass1 != this.pass2) {
      console.log('Not Equal');
      this.warn = "Passwords Don't match";
      this.disabled = 'hidden';
    } else {
      this.warn = '';
      this.disabled = 'submit';
    }
    if (this.pass2 == '') {
      this.warn = '';
    }
  }

  @action
  setPass(event) {
    this.pass1 = event.target.value;
  }
}
