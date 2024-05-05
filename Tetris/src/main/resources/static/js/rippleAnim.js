document.querySelectorAll('.button').forEach(btn => {
    btn.addEventListener('click', function(e) {
        // Get the button's position relative to the viewport
        let rect = this.getBoundingClientRect();
        let x = e.clientX - rect.left; // X position within the element.
        let y = e.clientY - rect.top;  // Y position within the element.

        let ripples = document.createElement('span');
        ripples.classList.add('ripple');
        ripples.style.left = x + 'px';
        ripples.style.top = y + 'px';
        this.appendChild(ripples);

        // Remove the ripple element after the animation
        setTimeout(() => {
            ripples.remove();
        }, 1000);
    });
});
