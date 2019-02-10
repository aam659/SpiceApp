/* Search Bar Function */
document.getElementById('searchform').onsubmit = function() {
    window.location = "http://www.google.com/search?q=site:" + document.getElementById('search').nodeValue;
    return false;
}