angular.module('BookmarkService', ['ngResource'])

  .factory('BookmarkResource', function($resource) {
    
    var bookmarks = $resource(
      'rest/bookmarks/:param1/:param2',
      {
        'param1': ''
      , 'param2': ''
      }, {
        'update': { 'method': 'PUT' }
      }
    );

    return bookmarks;
  	  
  });

// http://docs.angularjs.org/api/ngResource.$resource