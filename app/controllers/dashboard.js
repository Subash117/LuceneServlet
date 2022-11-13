import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';
import { A } from '@ember/array';


export default class DashboardController extends Controller {
  filesID = [];
  @tracked pages=A();
  @tracked currentPage;
  @tracked arrayOfArrays=A();
  @tracked currentLogs=A();

  @action toggle(fileId) {
    var test = document.getElementById(fileId).checked;
    if (test)
    {
      this.filesID.push(fileId);
    }
    else
    {
      var ind = this.filesID.indexOf(fileId);
      if (ind > -1) this.filesID.splice(ind, 1);
    }
  }


  @action
  async getResults()
  {
    var query = document.getElementById('query').value;
    var logs=A();
    this.pages=A();
    var k=1;
    for (var i = 0; i < this.filesID.length; i++)
    {
      let response = await fetch(`http://localhost:8080/LuceneWithServlet/querySearch?query=${query}&fileid=${this.filesID[i]}`,{credentials:'include'});
      let data = await response.json();
      for (var j = 0; j < data.logs.length; j++) 
        logs.pushObject(data.logs[j]);
    }

    for (var i=0; i<logs.length; i+=5) 
    {
      this.arrayOfArrays.pushObject(logs.slice(i,i+5));
      this.pages.pushObject(k++);
    }

    this.currentLogs=this.arrayOfArrays[0];
    this.currentPage=1;
  }

  @action
  changePage(pageNo)
  {
    this.currentPage=pageNo;
    this.currentLogs=this.arrayOfArrays[pageNo-1];
    console.log(pageNo+" "+this.currentPage);
  }
}
