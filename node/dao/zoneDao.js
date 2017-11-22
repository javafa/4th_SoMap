var cons = require("../modules/database");

var task = {
    select : function(callback){
        var query_str = "select * from crawl_zone";
        cons.query(query_str, function(error, items, field){
            callback(error, items);
        });
    },
    delete : function(callback){

    }
};

module.exports = task;


