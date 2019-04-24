export default function(jQuery){
    return {
        configure: function(formId){
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
                });
            });
        }
    };
};