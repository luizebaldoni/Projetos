function alerta_vermelho() {
    window.alert('Mantenha-se ligade nas nossas atualizações semanais em playlists e muito mais!');
}
// ADICIONANDO AÇÕES DO MENU DAS PAGINAS
const navSlide = () => {
    const burger = document.querySelector(".burger");
    const nav = document.querySelector(".nav-links");
    const navLinks = document.querySelectorAll(".nav-links a");
    burger.addEventListener("click", () => {
      nav.classList.toggle("nav-active");

      navLinks.forEach((link, index) => {
        if (link.style.animation) {
          link.style.animation = "";
        } else {
          link.style.animation = `navLinkFade 0.5s ease forwards ${
            index / 7 + 0.5
          }s `;
        }
      });
      burger.classList.toggle("toggle");
    });
  }
 // MODAL
const modal = () =>{
  var modal = document.getElementById("myModal");
  var btn = document.getElementById("myBtn");
  var span = document.getElementsByClassName("close")[0];
  btn.onclick = function() {
    modal.style.display = "block";
  }
  span.onclick = function() {
    modal.style.display = "none";
  }
  window.onclick = function(event) {
    if (event.target == modal) {
      modal.style.display = "none";
    }
  }
}
// AJAX:
