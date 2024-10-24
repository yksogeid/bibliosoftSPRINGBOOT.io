// modalHandler.js

const editModal = document.getElementById('editModal');
editModal.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget;  // Botón que disparó el modal
    const id = button.getAttribute('data-id');
    const nombre = button.getAttribute('data-nombre');
    const apellido = button.getAttribute('data-apellido');
    const email = button.getAttribute('data-email');

    // Actualizar los campos del modal con los datos del usuario
    document.getElementById('edit-id').value = id;
    document.getElementById('edit-nombre').value = nombre;
    document.getElementById('edit-apellido').value = apellido;
    document.getElementById('edit-email').value = email;
});
console.log({ id, nombre, apellido, email });
