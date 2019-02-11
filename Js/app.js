/* Search Bar Function */
document.getElementById('searchform').onsubmit = function() {
    window.location = "http://www.google.com/search?q=site:http://127.0.0.1:5500/Html" + document.getElementById('search').nodeValue;
    return false;
}