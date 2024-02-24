  // Get references to the popup and close button
            var popup = document.getElementById('popupContainer');
            var closePopup = document.getElementById('closePopup');

            // Get reference to the open button
            var openPopup = document.getElementById('openPopup');

            // Function to open the popup
            function openPopupFunction() {
                popup.style.display = 'block';
            }

            // Function to close the popup
            function closePopupFunction() {
                popup.style.display = 'none';
            }

            // Add click event listeners
            openPopup.addEventListener('click', openPopupFunction);
            closePopup.addEventListener('click', closePopupFunction);
