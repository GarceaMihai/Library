function search_book() {
  let input = document.getElementById('searchbar').value;
  input=input.toLowerCase();
  let x = document.getElementsByClassName('books');

  for (i = 0; i < x.length; i++) {
    x[i].style.display="list-item";
    if (input.length >= 3) {
      if (!x[i].innerHTML.toLowerCase().includes(input)) {
        x[i].style.display="none";
      }
    }
  }
}
