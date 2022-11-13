import EmberRouter from '@ember/routing/router';
import config from 'zoho-project-test/config/environment';

export default class Router extends EmberRouter {
  location = config.locationType;
  rootURL = config.rootURL;
}

Router.map(function () {
  this.route('login');
  this.route('signup');
  // this.route('dashboard', { path: '/dashboard/:id/:sesid' });
  this.route('dashboard');
  this.route('log-redirect');
});
