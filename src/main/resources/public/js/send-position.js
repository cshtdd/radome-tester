export default function(jQuery){
    function showAlert(id, text){
        jQuery(`${id} .message`).text(text);
        jQuery(id).show();
        setTimeout(function(){
            jQuery(id).hide();
        }, 3000);
    }

    return {
        configure: function(config){
            /*
            {
                formId,
                errorMessageId
            }
            */

            console.log('Configure send position');

            jQuery(`${config.formId} button`).click(function(e){
                e.preventDefault();
                e.stopPropagation();

                jQuery(config.formId).addClass('was-validated');

                if (jQuery(config.formId)[0].checkValidity() === false) {
                    console.log('invalid input');
                    return;
                }

                var theta = parseInt(jQuery(`${config.formId} #inputTheta`).val());
                var phi = parseInt(jQuery(`${config.formId} #inputPhi`).val());

                console.log(`Button Set Position Clicked; theta: ${theta}; phi: ${phi};`);

                // //TODO validate input values
                // if (isNaN(theta)){
                //     setCustomValidity('Invalid Theta');
                // }
                // else {
                //     setCustomValidity('');
                // }

                jQuery.ajax('/api/movement', {
                    data: JSON.stringify({ thetaDegrees: theta, phiDegrees: phi }),
                    contentType: 'application/json',
                    type: 'POST'
                }).done(function(r){
                    console.log('position sent; response: ', r);
                }).fail(function(e) {
                    console.log('transmission error: ', e);
                    var errorMessage = `[${e.status}] - ${e.statusText}`;
                    showAlert(config.errorMessageId, errorMessage);
                });
            });
        }
    };
};