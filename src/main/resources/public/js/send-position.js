export default function(jQuery){
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

                var theta = jQuery(`${config.formId} #inputTheta`).val();
                var phi = jQuery(`${config.formId} #inputPhi`).val();

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
                    jQuery(`${config.errorMessageId} .message`).text(errorMessage);
                    jQuery(config.errorMessageId).show();
                    setTimeout(function(){
                        jQuery(config.errorMessageId).hide();
                    }, 3000);
                });
            });
        }
    };
};