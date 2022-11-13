import Route from '@ember/routing/route';
import { service } from '@ember/service';

export default class DashboardRoute extends Route {

  @service router;
  async beforeModel()
  {
    let response1=await fetch(`http:///localhost:8080/LuceneWithServlet/getsession`,{credentials:'include'});
    let data1=await response1.json();
    if(!data1.session)
    {
      this.router.transitionTo('login');
    }
  }
  
  async model() {
    let response = await fetch(`http://localhost:8080/LuceneWithServlet/files`,{credentials:'include'});
    let data = await response.json();
    return data;
  }
}
