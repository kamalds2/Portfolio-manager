// Dismiss flash message and clear from session
function dismissFlashMessage(button) {
    const flashMessage = button.parentElement;
    
    // Add hiding animation
    flashMessage.classList.add('hiding');
    
    // Remove from DOM after animation
    setTimeout(() => {
        flashMessage.remove();
    }, 300);
    
    // Clear from backend session
    fetch('/api/clear-flash', {
        method: 'POST',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            console.log('✅ Flash message cleared from session');
        } else {
            console.log('⚠️ Failed to clear flash message:', response.status);
        }
    })
    .catch(err => {
        console.log('❌ Error clearing flash message from session:', err);
    });
}

document.addEventListener("DOMContentLoaded", function () {
    // Auto-dismiss flash messages after 5 seconds with animation
    const flashMessages = document.querySelectorAll('.flash-message');
    flashMessages.forEach(message => {
        setTimeout(() => {
            // Find the close button and trigger dismissal
            const closeButton = message.querySelector('.flash-close');
            if (closeButton) {
                dismissFlashMessage(closeButton);
            } else {
                message.classList.add('hiding');
                setTimeout(() => {
                    message.remove();
                }, 300);
            }
        }, 5000);
    });

    // Confirm delete for elements using .delete-link
    document.querySelectorAll(".delete-link").forEach(link => {
        link.addEventListener("click", function (e) {
            if (!confirm("Are you sure you want to delete this item?")) {
                e.preventDefault();
            }
        });
    });

    // DataTables auto-init for tables with .datatable or .table within .table-container
    if (window.jQuery && typeof jQuery.fn.DataTable === 'function') {
        jQuery('.table-container table, table.datatable, table.table').each(function () {
            const $tbl = jQuery(this);
            if ($tbl.hasClass('dataTable')) return; // already initialized
            $tbl.DataTable({
                pageLength: 10,
                lengthMenu: [5,10,25,50,100],
                order: [],
                autoWidth: false,
                language: {
                    search: "Filter:",
                }
            });
        });
    }

    // Place primary action (Add button) to the right in page header
    document.querySelectorAll('.page-header').forEach(ph => {
        ph.style.gap = '0.75rem';
    });

    // Profile form: Handle image URL vs file upload interaction
    const profileImageUrl = document.getElementById('profileImage');
    const imageFileInput = document.getElementById('imageFile');
    
    if (profileImageUrl && imageFileInput) {
        // When user uploads a file, clear the URL field
        imageFileInput.addEventListener('change', function() {
            if (this.files && this.files.length > 0) {
                profileImageUrl.value = '';
                showToast('File selected. URL field cleared.', 'info');
            }
        });
        
        // When user enters a URL, clear the file input
        profileImageUrl.addEventListener('input', function() {
            if (this.value.trim() !== '') {
                imageFileInput.value = '';
            }
        });
    }

    // Resume form: Handle resume URL vs file upload interaction
    const resumeUrl = document.getElementById('resumeUrl');
    const resumeFileInput = document.getElementById('resumeFile');
    
    if (resumeUrl && resumeFileInput) {
        // When user uploads a file, clear the URL field
        resumeFileInput.addEventListener('change', function() {
            if (this.files && this.files.length > 0) {
                resumeUrl.value = '';
                showToast('Resume file selected. URL field cleared.', 'info');
            }
        });
        
        // When user enters a URL, clear the file input
        resumeUrl.addEventListener('input', function() {
            if (this.value.trim() !== '') {
                resumeFileInput.value = '';
            }
        });
    }
});

// Toast notification function
function showToast(message, type = 'info') {
    // Create toast container if it doesn't exist
    let toastContainer = document.querySelector('.toast-container');
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container';
        document.body.appendChild(toastContainer);
    }

    // Create toast element
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
        <div class="toast-content">
            <i class="fas fa-${type === 'success' ? 'check-circle' : type === 'error' ? 'exclamation-circle' : 'info-circle'}"></i>
            <span>${message}</span>
        </div>
        <button class="toast-close" onclick="this.parentElement.remove()">
            <i class="fas fa-times"></i>
        </button>
    `;

    // Add to container
    toastContainer.appendChild(toast);

    // Auto-remove after 4 seconds
    setTimeout(() => {
        if (toast && toast.parentElement) {
            toast.remove();
        }
    }, 4000);
}

// Session keepalive functionality
let sessionKeepAliveInterval;
let userActivityTimeout;
const KEEPALIVE_INTERVAL = 10 * 60 * 1000; // 10 minutes
const ACTIVITY_TIMEOUT = 23 * 60 * 60 * 1000; // 23 hours (just before 24h timeout)

function initializeSessionKeepAlive() {
    // Send keepalive request to server
    function sendKeepAlive() {
        fetch('/api/keepalive', {
            method: 'GET',
            credentials: 'same-origin'
        }).then(response => {
            if (!response.ok) {
                console.log('Session expired, redirecting to login');
                window.location.href = '/login?expired';
            }
        }).catch(() => {
            // If keepalive fails, session might be expired
            console.log('Session keepalive failed');
        });
    }

    // Reset activity timer
    function resetActivityTimer() {
        clearTimeout(userActivityTimeout);
        userActivityTimeout = setTimeout(() => {
            // Show warning 1 hour before timeout
            showSessionWarning();
        }, ACTIVITY_TIMEOUT - (60 * 60 * 1000)); // 22 hours
    }

    // Show session expiration warning
    function showSessionWarning() {
        // Create a more user-friendly modal instead of browser confirm
        const modal = document.createElement('div');
        modal.className = 'session-warning-modal';
        modal.innerHTML = `
            <div class="session-warning-content">
                <div class="session-warning-header">
                    <i class="fas fa-clock"></i>
                    <h3>Session Expiring Soon</h3>
                </div>
                <p>Your session will expire in 1 hour due to inactivity.</p>
                <p>Would you like to continue working?</p>
                <div class="session-warning-buttons">
                    <button class="btn btn-primary" onclick="continueSession()">
                        <i class="fas fa-check"></i> Continue Session
                    </button>
                    <button class="btn btn-secondary" onclick="logoutNow()">
                        <i class="fas fa-sign-out-alt"></i> Logout Now
                    </button>
                </div>
            </div>
        `;
        
        // Add modal styles if not already added
        if (!document.querySelector('#session-warning-styles')) {
            const style = document.createElement('style');
            style.id = 'session-warning-styles';
            style.textContent = `
                .session-warning-modal {
                    position: fixed;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    background: rgba(0,0,0,0.7);
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    z-index: 10001;
                }
                .session-warning-content {
                    background: white;
                    padding: 2rem;
                    border-radius: 12px;
                    max-width: 400px;
                    text-align: center;
                    box-shadow: 0 10px 30px rgba(0,0,0,0.3);
                }
                .session-warning-header {
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    gap: 0.5rem;
                    margin-bottom: 1rem;
                }
                .session-warning-header i {
                    color: #f59e0b;
                    font-size: 1.5rem;
                }
                .session-warning-buttons {
                    display: flex;
                    gap: 1rem;
                    margin-top: 1.5rem;
                    justify-content: center;
                }
            `;
            document.head.appendChild(style);
        }
        
        document.body.appendChild(modal);
        
        // Global functions for modal buttons
        window.continueSession = function() {
            modal.remove();
            resetActivityTimer();
            sendKeepAlive();
            showToast('Session extended successfully', 'success');
        };
        
        window.logoutNow = function() {
            modal.remove();
            window.location.href = '/logout';
        };
    }

    // Track user activity
    const activityEvents = ['mousedown', 'mousemove', 'keypress', 'scroll', 'touchstart', 'click'];
    let lastActivity = Date.now();

    activityEvents.forEach(event => {
        document.addEventListener(event, () => {
            const now = Date.now();
            // Only reset timer if it's been more than 1 minute since last activity
            if (now - lastActivity > 60000) {
                lastActivity = now;
                resetActivityTimer();
            }
        }, true);
    });

    // Start keepalive interval
    sessionKeepAliveInterval = setInterval(sendKeepAlive, KEEPALIVE_INTERVAL);
    
    // Initialize activity timer
    resetActivityTimer();
}

// Initialize session management when page loads
if (window.location.pathname !== '/login') {
    initializeSessionKeepAlive();
}

// Project images preview functionality
document.addEventListener('DOMContentLoaded', function() {
    const projectImagesInput = document.getElementById('projectImages');
    
    if (projectImagesInput) {
        projectImagesInput.addEventListener('change', function(event) {
            const files = event.target.files;
            
            if (files && files.length > 0) {
                // Create preview area if it doesn't exist
                let previewArea = document.querySelector('.image-preview-area');
                if (!previewArea) {
                    previewArea = document.createElement('div');
                    previewArea.className = 'image-preview-area';
                    previewArea.innerHTML = '<label>Selected Images Preview:</label><div class="preview-gallery"></div>';
                    projectImagesInput.parentNode.appendChild(previewArea);
                }
                
                const previewGallery = previewArea.querySelector('.preview-gallery');
                previewGallery.innerHTML = ''; // Clear existing previews
                
                // Add CSS if not already added
                if (!document.querySelector('#preview-styles')) {
                    const style = document.createElement('style');
                    style.id = 'preview-styles';
                    style.textContent = `
                        .image-preview-area {
                            margin-top: 1rem;
                            padding: 1rem;
                            background: var(--background);
                            border-radius: 8px;
                            border: 1px solid var(--border);
                        }
                        .preview-gallery {
                            display: grid;
                            grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
                            gap: 0.75rem;
                            margin-top: 0.5rem;
                        }
                        .preview-item {
                            position: relative;
                            background: var(--surface);
                            border-radius: 6px;
                            overflow: hidden;
                            border: 1px solid var(--border);
                        }
                        .preview-item img {
                            width: 100%;
                            height: 90px;
                            object-fit: cover;
                            display: block;
                        }
                        .preview-item .file-name {
                            font-size: 0.75rem;
                            color: var(--text-light);
                            padding: 0.25rem 0.5rem;
                            word-break: break-all;
                        }
                    `;
                    document.head.appendChild(style);
                }
                
                // Create preview for each selected file
                Array.from(files).forEach((file, index) => {
                    if (file.type.startsWith('image/')) {
                        const reader = new FileReader();
                        reader.onload = function(e) {
                            const previewItem = document.createElement('div');
                            previewItem.className = 'preview-item';
                            previewItem.innerHTML = `
                                <img src="${e.target.result}" alt="Preview ${index + 1}">
                                <div class="file-name">${file.name}</div>
                            `;
                            previewGallery.appendChild(previewItem);
                        };
                        reader.readAsDataURL(file);
                    }
                });
                
                showToast(`${files.length} image(s) selected for upload`, 'success');
            }
        });
    }
});