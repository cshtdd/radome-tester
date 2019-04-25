export default function(jQuery){
    return {
        configure: function(formId, errorMessageId){
            console.log('Configure send position');

            jQuery(`${formId} button`).click(function(e){
                e.preventDefault();

                var theta = jQuery(`${formId} #inputTheta`).val();
                var phi = jQuery(`${formId} #inputPhi`).val();

                console.log(`Button Set Position Clicked; theta: ${theta}; phi: ${phi};`);

                //TODO validate input values

                jQuery.ajax('/api/movement', {
                    data: JSON.stringify({ thetaDegrees: theta, phiDegrees: phi }),
                    contentType: 'application/json',
                    type: 'POST'
                }).done(function(r){
                    console.log('position sent; response: ', r);
                }).fail(function(e) {
                    console.log('transmission error: ', e);
                    var errorMessage = `[${e.status}] - ${e.statusText}`;
                    jQuery(`${errorMessageId} .message`).text(errorMessage);
                    jQuery(errorMessageId).show();
                    setTimeout(function(){
                        jQuery(errorMessageId).hide();
                    }, 3000);
                });
            });
        }
    };
};