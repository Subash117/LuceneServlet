import Route from '@ember/routing/route';
import { service } from '@ember/service';

export default class LoginRoute extends Route {
    @service router;
  async beforeModel()
  {
    let response1=await fetch(`http:///localhost:8080/LuceneWithServlet/getsession`,{credentials:'include'});
    let data1=await response1.json();
    if(data1.session)
    {
      this.router.transitionTo('dashboard');
    }
  }
}
