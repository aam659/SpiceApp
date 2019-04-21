/* Search Bar Function */
// document.getElementById('searchform').onsubmit = function() {
//     window.location = "http://www.google.com/search?q=site:http://127.0.0.1:5500/downloads.html" + document.getElementById('search').nodeValue.valueOf;
//     return false;
// }

//Enter domain of site to search.
let domainroot="https://aam659.github.io/SpiceApp/"

let searchaction=[ //form action for the 3 search engines
    "http://www.google.com/search",
    "http://search.yahoo.com/search",
    "http://search.msn.com/results.aspx"
]

let queryfieldname=["q","p","q"] //name of hidden query form for the 3 search engines

function switchaction(cur, index){
    cur.form.action=searchaction[index];
    document.getElementById("hiddenquery").name=queryfieldname[index];
}

function jksitesearch(curobj){
    for (i=0; i< document.jksearch.se.length; i++){ //loop through radio to see which is checked

        if (document.jksearch.se[i].checked==true)
            switchaction(document.jksearch.se[i], i);
    }
    
    document.getElementById("hiddenquery").value="site:"+domainroot+curobj.qfront.value;
}

