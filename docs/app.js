/* Search Bar Function */

// Enter domain of site to search.
// Only works if actual domain name
let domainroot="https://aam659.github.io/SpiceApp/";

// name of hidden query form for the 3 search engines
let queryfieldname="q";

function sitesearch(curobj){
    document.getElementById("hiddenquery").value="site:"+domainroot+ " " + curobj.qfront.value;
}

