export default function(jQuery){
    return {
        configure: function(formId){
            console.log('Configure send position');

            jQuery(`${formId} button`).click(function(e){
                e.preventDefault();

                var theta = jQuery(`${formId} #inputTheta`).val();
                var phi = jQuery(`${formId} #inputPhi`).val();

                console.log(`Button Set Position Clicked; theta: ${theta}; phi: ${phi};`);
            });
        }
    };
};