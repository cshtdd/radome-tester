export default function(jQuery){
    function showAlert(id, text){
        jQuery(`${id} .message`).text(text);
        jQuery(id).show();
        setTimeout(function(){
            jQuery(id).hide();
        }, 3000);
    }

    return {
        setupStop: function(config){
            /*
            {
                buttonId,
                errorMessageId,
                successMessageId
            }
            */

            console.log('Configure Movement Stop');

            jQuery(config.buttonId).click(function(e){
                e.preventDefault();
                e.stopPropagation();

                console.log(`Button Movement Stop Clicked`);

                jQuery.ajax('/api/movement/stop', {
                    contentType: 'application/json',
                    type: 'POST'
                }).done(function(r){
                    console.log('movement stop; response: ', r);
                    showAlert(config.successMessageId, 'Movement Stop');
                }).fail(function(e) {
                    console.log('transmission error: ', e);
                    var errorMessage = `[${e.status}] - ${e.responseText}`;
                    showAlert(config.errorMessageId, errorMessage);
                });
            });
        },
        setupStart: function(config){
            /*
            {
                formId,
                errorMessageId,
                successMessageId
            }
            */

            console.log('Configure Movement Start');

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

                console.log(`Button Movement Start Clicked; theta: ${theta}; phi: ${phi};`);

                jQuery.ajax('/api/movement/start', {
                    data: JSON.stringify({ theta: theta, phi: phi }),
                    contentType: 'application/json',
                    type: 'POST'
                }).done(function(r){
                    console.log('position sent; response: ', r);
                    showAlert(config.successMessageId, 'Position Sent');
                }).fail(function(e) {
                    console.log('transmission error: ', e);
                    var errorMessage = `[${e.status}] - ${e.responseText}`;
                    showAlert(config.errorMessageId, errorMessage);
                });
            });
        }
    };
};