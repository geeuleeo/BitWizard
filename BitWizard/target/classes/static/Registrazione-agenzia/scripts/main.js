/**
 * 
 */


document.getElementById('signupForm').addEventListener('submit', function(event) {
	event.preventDefault();

	const partitaIVA = document.getElementById('partitaIVA').value;
	const partitaIVAError = document.getElementById('partitaIVAError');

	// Regular expression to validate Partita IVA
	const partitaIVARegex = /^IT\d{11}$/;

	if (!partitaIVARegex.test(partitaIVA)) {
		partitaIVAError.style.display = 'block';
		return; // Stop form submission if validation fails
	} else {
		partitaIVAError.style.display = 'none'; // Hide error message if valid
	}

	const formData = new FormData(this);

	fetch('/api/agenzia/signup', {
		method: 'POST',
		body: JSON.stringify(Object.fromEntries(formData)),
		headers: {
			'Content-Type': 'application/json'
		}
	})
		.then(response => {
			if (!response.ok) {
				throw new Error('Errore nella registrazione');
			}
			return response.text();
		})
		.then(data => {
			alert(data);
		})
		.catch(error => {
			alert(error.message);
		});
});
