/* FUNCIONES GENERALES */

function PasarDer(componenteIzq, componenteDer) {
    for (i = 0; i < document.getElementById(componenteIzq).length; i++) {
        if (document.getElementById(componenteIzq).options[i].selected) {
            var elem = document.createElement("option");
            elem.text = document.getElementById(componenteIzq).options[i].text;
            elem.value = document.getElementById(componenteIzq).options[i].value;
            document.getElementById(componenteDer).options.add(elem);
        }
    }
    for (i = document.getElementById(componenteIzq).length - 1; i >= 0; i--) {
        if (document.getElementById(componenteIzq).options[i].selected)
            document.getElementById(componenteIzq).remove(i);
    }
}

function PasarIzq(componenteDer, componenteIzq) {
    for (i = 0; i < document.getElementById(componenteDer).length; i++) {
        if (document.getElementById(componenteDer).options[i].selected) {
            var elem = document.createElement("option");
            elem.text = document.getElementById(componenteDer).options[i].text;
            elem.value = document.getElementById(componenteDer).options[i].value;
            document.getElementById(componenteIzq).options.add(elem);
        }
    }
    for (i = document.getElementById(componenteDer).length - 1; i >= 0; i--) {
        if (document.getElementById(componenteDer).options[i].selected)
            document.getElementById(componenteDer).remove(i);
    }
}

//-- Dena
function ponerUltimoElementoOrdenado(componente) {
    var cantidad = document.getElementById(componente).length;
    var valorUltimo = document.getElementById(componente).options[cantidad - 1].value;
    var textoUltimo = document.getElementById(componente).options[cantidad - 1].text;
    for (i = 0; i < document.getElementById(componente).length; i++) {
        var valor = document.getElementById(componente).options[i].value;
        var texto = document.getElementById(componente).options[i].text;
        if (textoUltimo < texto) {
            for (j = document.getElementById(componente).length - 1; j > i; j--) {
                document.getElementById(componente).options[j].value = document.getElementById(componente).options[j - 1].value;
                document.getElementById(componente).options[j].text = document.getElementById(componente).options[j - 1].text;
            }
            document.getElementById(componente).options[i].value = valorUltimo;
            document.getElementById(componente).options[i].text = textoUltimo;
            return;
        }
    }
}

//-- Alex
function moverElementosSeleccionados(componenteOrigen, componenteDestino, dejarSeleccionadoOrigen) {
    // Crear arreglo vac�o que almacenar� una referencia a los elementos que ser�n movidos.
    var seleccionados = [];
    // Agregar cada elemento seleccionado del componente origen al destino.
    for (i = 0; i < document.getElementById(componenteOrigen).length; i++) {
        var valor = document.getElementById(componenteOrigen).options[i].value;
        if (document.getElementById(componenteOrigen).options[i].selected && valor != "") {
            // Agregar la posici�n del elemento a un arreglo temporal para luego borrar solo los que est�n en este arreglo.
            seleccionados[seleccionados.length] = i;

            if (!existeElementoEnSelect(document.getElementById(componenteDestino), valor)) {
                var elem = document.createElement("option");
                elem.text = document.getElementById(componenteOrigen).options[i].text;
                //elem.value = document.getElementById(componenteOrigen).options[i].value;
                elem.value = valor;
                // Agregar al destino.
                document.getElementById(componenteDestino).options.add(elem);
            }
        }
    }
    // Borrar los elementos del componente origen que fueron agregados al destino.
    for (i = seleccionados.length - 1; i >= 0; i--) {
        if (document.getElementById(componenteOrigen).options[seleccionados[i]].selected)
            document.getElementById(componenteOrigen).remove(seleccionados[i]);
    }
    // Dejar seleccionado por defecto el primer elemento del componente origen.
    if (dejarSeleccionadoOrigen) {
        document.getElementById(componenteOrigen).options[0].selected = true;
    }
}

function existeElementoEnSelect(select, valorElemento) {
    var encontrado = false;
    var i = 0;
    while ((i < select.length) && (encontrado == false)) {
        if (select.options[i].value == valorElemento) {
            encontrado = true;
        }
        i++;
    }
    return encontrado;
}

function seleccionarElementoEnSelect(componenteSelect, valorElemento) {
    var combo = document.getElementById(componenteSelect);
    if (combo != null) {
        var encontrado = false;
        var i = 0;
        while ((i < combo.length) && (encontrado == false)) {
            combo.options[i].selected = false;
            if (combo.options[i].value == valorElemento) {
                encontrado = true;
                combo.options[i].selected = true;
            }
            i++;
        }
    }
}

function cantElementosSeleccionados(componenteSelect) {
    var result = 0;
    if (document.getElementById(componenteSelect) != null) {
        for (i = 0; i < document.getElementById(componenteSelect).length; i++) {
            if (document.getElementById(componenteSelect).options[i].selected) {
                result++;
            }
        }
    }
    return result;
}

function valorSeleccionadoSelectMultiple(componenteSelect) {
    if (document.getElementById(componenteSelect) != null) {
        for (i = 0; i < document.getElementById(componenteSelect).length; i++) {
            if (document.getElementById(componenteSelect).options[i].selected) {
                return document.getElementById(componenteSelect).options[i].value;
            }
        }
    }
    return null;
}


function chequearElementosSeleccionados(componenteSelect) {
    var seleccionados = cantElementosSeleccionados(componenteSelect);

    if (seleccionados == 1) {
        // Hacer submit si est� seleccionado solamente un elemento.
        document.getElementById(componenteSelect).form.submit();
    } else if (seleccionados == 0) {
        alert("Debe estar seleccionado alg�n elemento.");
    } else if (seleccionados > 1) {
        alert("Debe estar seleccionado solamente un elemento.");
    }
}

function marcarSelectsMultiples() {
    if (document.getElementsByTagName) {
        // Seleccionar todos los componentes SELECT.
        var s = document.getElementsByTagName("select");
        if (s.length > 0) {
            for (var i = 0; i < s.length; i++) {
                // Solo procesar los Selects Multiples.
                if (s[i].multiple == true) marcarSelects(s[i].id);
            }
        }
    }
}

function desmarcarSelectsMultiples() {
    if (document.getElementsByTagName) {
        // Seleccionar todos los componentes SELECT.
        var s = document.getElementsByTagName("select");
        if (s.length > 0) {
            for (var i = 0; i < s.length; i++) {
                // Solo procesar los Selects Multiples.
                if (s[i].multiple == true) {
                    desmarcarSelects(s[i].id);
                }
            }
        }
    }
}

function desmarcarSelects(componente) {
    if (document.getElementById(componente) != null)
        for (i = 0; i < document.getElementById(componente).length; i++)
            document.getElementById(componente).options[i].selected = false;
}

//-- Alex

function marcarSelects(componente) {
    if (document.getElementById(componente) != null)
        for (i = 0; i < document.getElementById(componente).length; i++)
            document.getElementById(componente).options[i].selected = true;
}

function cantChBoxSeleccionados(form) {
    var total = 0;
    for (i = 0; i < form.elements.length; i++) {
        if (form.elements[i].checked == true) {
            total += 1;
        }
    }
    return total;
}


function desmarcarChBox(value) {
    var total = 0;
    var campos = document.getElementsByTagName("input");

    var i;
    for (i = 0; i < campos.length; i++) {
        if (campos[i].type == 'checkbox' && campos[i].checked == true && campos[i].value != value) {
            campos[i].checked = false;
        }
    }

}

function cantChBoxMarcados() {
    var total = 0;
    var campos = document.getElementsByTagName("input");
    var existe
    var i;
    for (i = 0; i < campos.length; i++) {
        if (campos[i].type == 'checkbox' && campos[i].checked == true) {
            total += 1;
        }
    }
    return total;
}

function existeUnChBoxSeleccinado(mensaje) {
    if (cantChBoxMarcados() == 0) {
        alert(mensaje);
    }

}

function cantChBoxSeleccionadosPorId(form, idCheck) {
    var total = 0;
    for (i = 0; i < form.elements.length; i++) {
        if (form.elements[i].id == idCheck && form.elements[i].checked == true) {
            total += 1;
        }
    }
    return total;
}

function cantChBoxSeleccionadosPorNombre(nombreChecks) {
    var total = 0;
    var checks = document.getElementsByName(nombreChecks);
    for (var i = 0; i < checks.length; i++) {
        if (checks[i].checked == true) {
            total += 1;
        }
    }
    return total;
}

function alMenosUnCheckBoxSeleccionado(form, idCheck, mensaje) {
    if (cantChBoxSeleccionadosPorId(form, idCheck) < 1) {
        alert(mensaje);
        return false;
    } else {
        return true;
    }
}

function asignarValorCampo(campo, valor) {
    document.getElementById(campo).value = valor;
}

//asignar el valor "valor2" al campo2 cuando el campo campo1 tome el valor valor1
function asignarValorPorValorCampo(campo2, valor2, campo1, valor1) {
    if (document.getElementById(campo1).value == valor1) {
        asignarValorCampo(campo2, valor2);
    }
}

//asignar el valor "valor2" al campo2 cuando el campo campo1 no tenga el valor valor1
function asignarValorPorNoValorCampo(campo2, valor2, campo1, valor1) {
    if (document.getElementById(campo1).value != valor1) {
        asignarValorCampo(campo2, valor2);
    }
}

//devuelve el valor  del Radio "nombre" seleccionado
function obtenerRadioSeleccionado(nombre) {
    var seleccionado = -1;
    var radios = document.getElementsByName(nombre);
    var i = 0;
    while ((seleccionado == -1) && (i < radios.length)) {
        if (radios.item(i).checked) {
            seleccionado = radios.item(i).value;
        }
        i++;
    }
    return seleccionado;
}

function checkPassword(clave, repetirClave, mensajeError) {
    var c = document.getElementById(clave);
    var rc = document.getElementById(repetirClave);
    if ((c.value != "") && (rc.value != "")) {
        if (c.value != rc.value) {
            alert(mensajeError);
            return false;
        }
    }
    return true;
}

function getBrowserHeight(varWindow) {
    var intH = 0;
    var intW = 0;
    if (typeof varWindow.innerWidth == 'number') {
        intH = varWindow.innerHeight;
        intW = varWindow.innerWidth;
    } else if (varWindow.document.documentElement && (varWindow.document.documentElement.clientWidth || varWindow.document.documentElement.clientHeight)) {
        intH = varWindow.document.documentElement.clientHeight;
        intW = varWindow.document.documentElement.clientWidth;
    } else if (varWindow.document.body && (varWindow.document.body.clientWidth || varWindow.document.body.clientHeight)) {
        intH = varWindow.document.body.clientHeight;
        intW = varWindow.document.body.clientWidth;
    }

    return {width: parseInt(intW), height: parseInt(intH)};
}

function setLayerPosition(varWindow) {
    var shadow = varWindow.document.getElementById("shadow");
    var shadowFrame = varWindow.document.getElementById("shadowFrame");
    var loadPanel = varWindow.document.getElementById("loadPanel");

    var bws = getBrowserHeight(varWindow);
    shadow.style.width = bws.width + "px";
    shadow.style.height = bws.height + "px";
    shadowFrame.style.width = bws.width + "px";
    shadowFrame.style.height = bws.height + "px";

    loadPanel.style.left = parseInt((bws.width - 400) / 2);
    loadPanel.style.top = parseInt((bws.height - 300) / 2);

    shadow = null;
    shadowFrame = null;
    loadPanel = null;
}

function showLayer(varWindow) {
    //setLayerPosition();

    var shadow = varWindow.document.getElementById("shadow");
    var shadowFrame = varWindow.document.getElementById("shadowFrame");
    var loadPanel = varWindow.document.getElementById("loadPanel");

    shadow.style.display = "block";
    shadowFrame.style.display = "block";
    loadPanel.style.display = "block";

    shadow = null;
    shadowFrame = null;
    loadPanel = null;
    setLayerPosition(varWindow);
    // varWindow.onresize = setLayerPosition(varWindow);
}

function hideLayer() {
    var shadow = document.getElementById("shadow");
    var loadPanel = document.getElementById("loadPanel");

    shadow.style.display = "none";
    loadPanel.style.display = "none";

    shadow = null;
    loadPanel = null;
}

function obtenerChecksSeleccionados(nombreChecks) {
    var checks = document.getElementsByName(nombreChecks);
    var ids = [];
    for (var i = 0; i < checks.length; i++) {
        if (checks[i].checked == true) {
            ids.push(checks[i].value)
        }
    }
    return ids;
}

//ocultar o mostrar el campo con id "campo" de acuerdo al valor de "oculto"
function ocultarCampo(campo, oculto) {
    if (oculto) {
        document.getElementById(campo).style.visibility = 'hidden';
    } else {
        document.getElementById(campo).style.visibility = 'visible';
    }
}

//deshabilitar o habilitar el campo con id "campo" de acuerdo al valor de "deshabilitado"
function deshabilitarCampo(campo, deshabilitado) {
    if (document.getElementById(campo) != null) {
        if (deshabilitado) {
            document.getElementById(campo).disabled = 'disabled';
        } else {
            document.getElementById(campo).disabled = '';
        }
    }
}

//ocultar o mostrar los campos del arreglo de ids "campos" de acuerdo al valor de "oculto"
function ocultarCampos(campos, oculto) {
    for (var i = 0; i < campos.length; i++) {
        ocultarCampo(campos[i], oculto);
    }
}

//deshabilitar o habilitar los campos del arreglo de ids "campos" de acuerdo al valor de "deshabilitado"
function deshabilitarCampos(campos, deshabilitado) {
    for (var i = 0; i < campos.length; i++) {
        deshabilitarCampo(campos[i], deshabilitado);
    }
}

//deshabilitar o habilitar el grupo de campos con id "grupo" de acuerdo al valor de "deshabilitado"
function deshabilitarGrupo(grupo, deshabilitado) {
    try {
        var g = document.getElementById(grupo);
        var campos = g.getElementsByTagName("input");
        var camposS = g.getElementsByTagName("select");
        var camposI = g.getElementsByTagName("img");
        var i;
        if (deshabilitado) {
            for (i = 0; i < campos.length; i++) {
                campos[i].disabled = 'disabled';
            }
            for (i = 0; i < camposS.length; i++) {
                camposS[i].disabled = 'disabled';
            }
            for (i = 0; i < camposI.length; i++) {
                camposI[i].disabled = 'disabled';
            }
        } else {
            for (i = 0; i < campos.length; i++) {
                campos[i].disabled = '';
            }
            for (i = 0; i < camposS.length; i++) {
                camposS[i].disabled = '';
            }
            for (i = 0; i < camposI.length; i++) {
                camposI[i].disabled = '';
            }
        }
    } catch (err) {
    }
}

//ocultar o mostrar el grupo de campos con id "grupo" de acuerdo al valor de "oculto"
function ocultarGrupo(grupo, oculto) {
    var g = document.getElementById(grupo);
    if (g !== null) {
        if (oculto) {
            g.style.display = 'none';
        } else {
            g.style.display = '';
        }
    }
}

//ocultar o mostrar (dependiendo del valor de "oculto") el campo con id "campo" si el valor del campo con id "campo1" tiene el valor "valor"
function ocultarCampoPorValor(campo, oculto, campo1, valor) {
    var c = document.getElementById(campo1);
    if (c.value == valor) {
        ocultarCampo(campo, oculto);
    } else {
        ocultarCampo(campo, !oculto);
    }
}

//deshabilitar o habilitar (dependiendo del valor de "deshabilitado") el campo con id "campo" si el valor del campo con id "campo1" tiene el valor "valor"
function deshabilitarCampoPorValor(campo, deshabilitado, campo1, valor) {
    var c = document.getElementById(campo1);
    if (c.value == valor) {
        deshabilitarCampo(campo, deshabilitado);
    } else {
        deshabilitarCampo(campo, !deshabilitado);
    }
}

//ocultar o mostrar (dependiendo del valor de "oculto") el campo con id "campo" si el valor del campo con id "campo1" es alguno de los elementos del arreglo "valores"
function ocultarCampoPorValores(campo, oculto, campo1, valores) {
    var c = document.getElementById(campo1);
    if (arrayContainsValue(valores, c.value)) {
        ocultarCampo(campo, oculto);
    } else {
        ocultarCampo(campo, !oculto);
    }
}

//deshabilitar o habilitar (dependiendo del valor de "deshabilitado") el campo con id "campo" si el valor del campo con id "campo1" es alguno de los elementos del arreglo "valores"
function deshabilitarCampoPorValores(campo, deshabilitado, campo1, valores) {
    var c = document.getElementById(campo1);
    if (arrayContainsValue(valores, c.value)) {
        deshabilitarCampo(campo, deshabilitado);
    } else {
        deshabilitarCampo(campo, !deshabilitado);
    }
}

//ocultar o mostrar (dependiendo del valor de "oculto") el grupo de campos con id "grupo" si el valor del campo con id "campo" tiene el valor "valor"
function ocultarGrupoPorValor(grupo, oculto, campo, valor) {
    var c = document.getElementById(campo);
    if (c.value == valor) {
        ocultarGrupo(grupo, oculto);
    } else {
        ocultarGrupo(grupo, !oculto);
    }
}

//deshabilitar o habilitar (dependiendo del valor de "deshabilitado") el grupo de campos con id "grupo" si el valor del campo con id "campo" tiene el valor "valor"
function deshabilitarGrupoPorValor(grupo, deshabilitado, campo, valor) {
    var c = document.getElementById(campo);
    if (c.value == valor) {
        deshabilitarGrupo(grupo, deshabilitado);
    } else {
        deshabilitarGrupo(grupo, !deshabilitado);
    }
}

//ocultar o mostrar (dependiendo del valor de "oculto") los campos del arreglo de ids "campos" si el valor del campo con id "campo" tiene el valor "valor"
function ocultarCamposPorValor(campos, oculto, campo, valor) {
    var c = document.getElementById(campo);
    if (c.value == valor) {
        ocultarCampos(campos, oculto);
    } else {
        ocultarCampos(campos, !oculto);
    }
}

//deshabilitar o habilitar (dependiendo del valor de "deshabilitado") los campos del arreglo de ids "campos" si el valor del campo con id "campo" tiene el valor "valor"
function deshabilitarCamposPorValor(campos, deshabilitado, campo, valor) {
    var c = document.getElementById(campo);
    if (c.value == valor) {
        deshabilitarCampos(campos, deshabilitado);
    } else {
        deshabilitarCampos(campos, !deshabilitado);
    }
}

//ocultar o mostrar (dependiendo del valor de "oculto") el grupo de campos con id "grupo" si el valor del campo con id "campo" es alguno de los elementos del arreglo "valores"
function ocultarGrupoPorValores(grupo, oculto, campo, valores) {
    var c = document.getElementById(campo);
    if (arrayContainsValue(valores, c.value)) {
        ocultarGrupo(grupo, oculto);
    } else {
        ocultarGrupo(grupo, !oculto);
    }
}

//deshabilitar o habilitar (dependiendo del valor de "deshabilitado") el grupo de campos con id "grupo" si el valor del campo con id "campo" es alguno de los elementos del arreglo "valores"
function deshabilitarGrupoPorValores(grupo, deshabilitado, campo, valores) {
    var c = document.getElementById(campo);
    if (arrayContainsValue(valores, c.value)) {
        deshabilitarGrupo(grupo, deshabilitado);
    } else {
        deshabilitarGrupo(grupo, !deshabilitado);
    }
}

//ocultar o mostrar (dependiendo del valor de "oculto") los campos del arreglo de ids "campos" si el valor del campo con id "campo" es alguno de los elementos del arreglo "valores"
function ocultarCamposPorValores(campos, oculto, campo, valores) {
    var c = document.getElementById(campo);
    if (arrayContainsValue(valores, c.value)) {
        ocultarCampos(campos, oculto);
    } else {
        ocultarCampos(campos, !oculto);
    }
}

//deshabilitar o habilitar (dependiendo del valor de "deshabilitado") los campos del arreglo de ids "campos" si el valor del campo con id "campo" es alguno de los elementos del arreglo "valores"
function deshabilitarCamposPorValores(campos, deshabilitado, campo, valores) {
    var c = document.getElementById(campo);
    if (arrayContainsValue(valores, c.value)) {
        deshabilitarCampos(campos, deshabilitado);
    } else {
        deshabilitarCampos(campos, !deshabilitado);
    }
}

//ocultar o mostrar (dependiendo del valor de "oculto") el campo con id "campo" si el campo (radio o checkbox) con id "chk" est� chequeado o no
function ocultarCampoPorChk(campo, oculto, chk, chequeado) {
    var c = document.getElementById(chk);
    if (c.checked == chequeado) {
        ocultarCampo(campo, oculto);
    } else {
        ocultarCampo(campo, !oculto);
    }
}

//deshabilitar o habilitar (dependiendo del valor de "deshabilitado") el campo con id "campo" si el campo (radio o checkbox) con id "chk" est� chequeado o no
function deshabilitarCampoPorChk(campo, deshabilitado, chk, chequeado) {
    var c = document.getElementById(chk);
    if (c.checked == chequeado) {
        deshabilitarCampo(campo, deshabilitado);
    } else {
        deshabilitarCampo(campo, !deshabilitado);
    }
}

function deshabilitarCampoChk(campo, chk) {
    var c = document.getElementById(chk);
    if (c.checked == true) {
        deshabilitarCampo(campo, true);
    } else {
        deshabilitarCampo(campo, false);
    }
}

//ocultar o mostrar (dependiendo del valor de "oculto") el grupo de campos con id "grupo" si el campo (radio o checkbox) con id "chk" est� chequeado o no
function ocultarGrupoPorChk(grupo, oculto, chk, chequeado) {
    var c = document.getElementById(chk);
    if (c.checked == chequeado) {
        ocultarGrupo(grupo, oculto);
    } else {
        ocultarGrupo(grupo, !oculto);
    }
}

//deshabilitar o habilitar (dependiendo del valor de "deshabilitado") el grupo de campos con id "grupo" si el campo (radio o checkbox) con id "chk" est� chequeado o no
function deshabilitarGrupoPorChk(grupo, deshabilitado, chk, chequeado) {
    var c = document.getElementById(chk);
    if (c.checked == chequeado) {
        deshabilitarGrupo(grupo, deshabilitado);
    } else {
        deshabilitarGrupo(grupo, !deshabilitado);
    }
}

//ocultar o mostrar (dependiendo del valor de "oculto") los campos del arreglo de ids "campos"  si el campo (radio o checkbox) con id "chk" est� chequeado o no
function ocultarCamposPorChk(campos, oculto, chk, chequeado) {
    var c = document.getElementById(chk);
    if (c.checked == chequeado) {
        ocultarCampos(campos, oculto);
    } else {
        ocultarCampos(campos, !oculto);
    }
}

//deshabilitar o habilitar (dependiendo del valor de "deshabilitado") los campos del arreglo de ids "campos"  si el campo (radio o checkbox) con id "chk" est� chequeado o no
function deshabilitarCamposPorValor(campos, deshabilitado, chk, chequeado) {
    var c = document.getElementById(chk);
    if (c.checked == chequeado) {
        deshabilitarCampos(campos, deshabilitado);
    } else {
        deshabilitarCampos(campos, !deshabilitado);
    }
}

//verificar si el arreglo "array" contiene el elemento "valor"
function arrayContainsValue(array, value) {
    for (var i = 0; i < array.length; i++) {
        if (array[i] == value) {
            return true;
        }
    }
    return false;
}

//garantizar que solo se indoduzcan numeros enteros en el campo de texto "campo"
//para ivocarlo se debe hacer asi: onkeypress="return soloEnteros(event.keyCode, event.which, this);"
function soloEnteros($char, $mozChar, campo) {
    if ($mozChar != null) { // Look for a Mozilla-compatible browser
        if (($mozChar >= 48 && $mozChar <= 57) || $mozChar == 0 || $char == 8 || $mozChar == 13) {
            $RetVal = true;
            if ($mozChar >= 48 && $mozChar <= 57) {
                if (isNaN(Number(String(campo.value + String.fromCharCode($mozChar))))) {
                    $RetVal = false;
                }
            }
        } else {
            $RetVal = false;
        }
    } else { // Must be an IE-compatible Browser
        if (($char >= 48 && $char <= 57) || $char == 13) {
            if (isNaN(Number(String(campo.value + String.fromCharCode($char))))) {
                $RetVal = false;
            } else
                $RetVal = true;
        } else {
            $RetVal = false;
        }
    }

    return $RetVal;
}

//garantizar que solo se indoduzcan letras en el campo de texto "campo"
//para ivocarlo se debe hacer asi: onkeypress="return soloLetras(event.keyCode, event.which, this);"
function soloLetras($char, $mozChar, campo) {
    if ($mozChar != null) { // Look for a Mozilla-compatible browser
        if (($mozChar >= 65 && $mozChar <= 122) || $mozChar == 0 || $char == 8 || $mozChar == 13 || $mozChar == 32) {
            $RetVal = true;
        } else {
            $RetVal = false;
        }
    } else { // Must be an IE-compatible Browser
        if (($char >= 65 && $char <= 122) || $char == 13 || $char == 32) {
            $RetVal = true;
        } else {
            $RetVal = false;
        }
    }

    return $RetVal;
}


function verificarNavegadorIE() {
    var xmlHttp = null;
    try {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } catch (nav) {
        return false;
    }

    return true;
}


//garantizar que solo se indoduzcan numeros decimales en el campo de texto "campo"
//para ivocarlo se debe hacer asi: onkeypress="return soloDecimales(event.keyCode, event.which, this);"
function soloDecimales($char, $mozChar, campo) {
    if ($mozChar != null) { // Look for a Mozilla-compatible browser
        if (($mozChar >= 48 && $mozChar <= 57) || $mozChar == 0 || $char == 8 || $mozChar == 13 || $mozChar == 46) {
            $RetVal = true;
            if (($mozChar >= 48 && $mozChar <= 57) || $mozChar == 46) {
                if (isNaN(Number(String(campo.value + String.fromCharCode($mozChar))))) {
                    $RetVal = false;
                }
            }
        } else {
            $RetVal = false;
        }
    } else { // Must be an IE-compatible Browser
        if (($char >= 48 && $char <= 57) || $char == 13 || $char == 46) {
            if (isNaN(Number(String(campo.value + String.fromCharCode($char))))) {
                $RetVal = false;
            } else
                $RetVal = true;
        } else {
            $RetVal = false;
        }
    }

    return $RetVal;
}

//adiciona un cero al final del texto de un campo para numeros decimales cuyo valor termina en "."
function finalizarConCeroFloat(campo) {
    var valor = document.getElementById(campo).value;
    if (valor.charAt(valor.length - 1) == '.') {
        document.getElementById(campo).value = valor + '0';
    }
}

//adiciona un cero al incio del texto de un campo para numeros decimales que comienza con "."
function inicializarConCeroFloat(campo) {
    var valor = document.getElementById(campo).value;
    if (valor.charAt(0) == '.') {
        document.getElementById(campo).value = '0' + valor
    }
}

//colocar el foco en el campo con id "campo"
function colocarFoco(campo) {
    var c = document.getElementById(campo);
    if ((c.disabled != true) && (c.disabled != 'disabled') && (c.style.visibility != 'hidden'))
        document.getElementById(campo).focus();
}

//adicionar un evento onclick en los links de la cabecera de la tabla (displaytag) con atributo "htmlId" igual a "tablename"
//que permita hacer submit mientras se pagina y se ordena, de modo que no se pierdan los valores de los campos del formulario
var urlParameters;

function prepareTableHeader(tableName, idFormulario) {
    var table = document.getElementById(tableName);
    var links = table.getElementsByTagName("a");
    var form = document.getElementById(idFormulario);

    var targets = new Array(links.length);
    urlParameters = new Array(links.length);
    var baseaction = form.action;

    for (var i = 0; i < links.length; i++) {
        var curLink = links[i];
        curLink.id = i;
        targets[i] = curLink.search;
        curLink.href = "#";
        urlParameters[i] = targets[i].substring(1, targets[i].length);

        curLink.onclick = function () {
            if (baseaction.indexOf("?") == -1) {
                form.action = baseaction + "?" + urlParameters[this.id];
            } else {
                form.action = baseaction + "&" + urlParameters[this.id];
            }
            form.submit();
            //return false;
        };
    }
}

var urlParameters2;

function prepareTableHeader2(idTablaDisplay, idFormulario) {
    var table = document.getElementById(idTablaDisplay);
    if (table != null) {
        var links = table.getElementsByTagName("a");
        var form = document.getElementById(idFormulario);

        if (links.length > 0) {
            var targets = new Array(links.length);
            urlParameters2 = new Array(links.length);
            var baseaction = form.action;

            for (var i = 0; i < links.length; i++) {
                var curLink = links[i];
                if (curLink.id.search("_custom") == -1) {
                    curLink.id = i;
                    targets[i] = curLink.search;
                    curLink.href = "#";
                    urlParameters2[i] = targets[i].substring(1, targets[i].length);

                    curLink.onclick = function () {
                        if (baseaction.indexOf("?") == -1) {
                            baseaction += "?" + urlParameters2[this.id];
                        } else {
                            baseaction += "&" + urlParameters2[this.id];
                        }
                        submitAjax2(baseaction, '', idFormulario, 'cargandoTab');
                    };
                }
            }
        }
    }
}

function noSubmit() {
    return false;
}

/* FIN FUNCIONES GENERALES */

//funcion para cambiar de tabs utilizando un link en lugar de un boton.
function cambiarTab() {
    var tempDiv = document.getElementById('tempHidden');
    tempDiv.innerHTML = '<input type="hidden" name="method:cambiarTab" value="" id="tempField"/>';

    document.forms[0].submit();
}

//function para eliminar un elemento usando AJAX y el contenido se carga en un div
/**
 *  @author fpicayo
 *  action: El nombre del action
 *  metodo: El nomre de la funcion que se ejecutar� en el action
 *  campo: El id del campo oculto que pasar� como parametro
 *  div: El nomre del div que sera modificado
 */
function eliminarAjax(action, metodo, campo, div) {

    var idSeleccionado = document.getElementById(campo).value;
    var bindArgs = {
        url: action + "!" + metodo + ".action?idSeleccionado=" + idSeleccionado,
        load: function (type, data, evt) {
            var div_modificado = document.getElementById(div);
            div_modificado.innerHTML = data;
            if (dojo.byId("mensajes").innerHTML.search("table") == -1) {
                dojo.byId("mensajes").innerHTML = dojo.byId("mensajesExplotados").innerHTML;
                dojo.byId("mensajes").style.display = "none";
            } else {
                var d = dojo.byId("mensajes").innerHTML;
                div_modificado.innerHTML = "";
                div_modificado.innerHTML = data;
                dojo.byId("mensajes").innerHTML = "";
                dojo.byId("mensajes").style.display = "none";
                dojo.byId("mensajes").innerHTML = d;
            }
            //Element.show("mensajes");
            //setTimeout("Effect.Fade('mensajes');", 4000);
            document.getElementById("cargar").style.visibility = "hidden";
            Effect.BlindDown('mensajes');
            //setTimeout("Effect.DropOut('mensajes');", 3000);
            setTimeout("Effect.BlindUp('mensajes');", 4000);
            data.evalScripts();

        },
        mimetype: "text/plain"
    };
    dojo.io.bind(bindArgs);
}

/* limpia los campos "input" (exeptuando los botones), "select" y "textarea" de un formulario*/
function limpiarCampos() {
    var campos = document.getElementsByTagName("input");
    var camposS = document.getElementsByTagName("select");
    var camposI = document.getElementsByTagName("textarea");
    var i;
    for (i = 0; i < campos.length; i++) {
        if (campos[i].type != "submit") {
            campos[i].value = '';
        }
    }
    for (i = 0; i < camposS.length; i++) {
        camposS[i].options[0].selected = 'selected';
    }
    for (i = 0; i < camposI.length; i++) {
        camposI[i].value = '';
    }
}

function validarCamposVacios() {
    var campos = document.getElementsByTagName("input");
    var camposS = document.getElementsByTagName("select");
    var camposI = document.getElementsByTagName("textarea");

    alert(campos.length);
    var i;
    for (i = 0; i < campos.length; i++) {
        if (campos[i].type != "submit") {
            if (campos[i].type = "text") {
                if (campos[i].value == '') {
                    alert("No se ha podido efectuar la operaci�n debido a errores en los datos proporcionados");
                    return false;
                }
            }
        }
    }
    for (i = 0; i < camposS.length; i++) {
        if (campos[i].value == '') {
            alert("No se ha podido efectuar la operaci�n debido a errores en los datos proporcionados");
            return false;
        }
    }
    for (i = 0; i < camposI.length; i++) {
        if (campos[i].value == '') {
            alert("No se ha podido efectuar la operaci�n debido a errores en los datos proporcionados");
            return false;
        }
    }
    return true;

}


//function para realizar submit a un elemento usando AJAX y el contenido se carga en un div
/**
 *  @author fpicayo
 *  action: El nombre del action
 *  metodo: El nomre de la funcion que se ejecutar� en el action
 *  campo: El id del campo oculto que pasar� como parametro
 *  div: El nomre del div que sera modificado
 */
function submitAjax(action, metodo, campo, div, indicador) {
    var idSeleccionado = document.getElementById(campo).value;

    if (document.getElementById(indicador) != null) {
        ocultarCampo(indicador, false);
    }
    var bindArgs = {
        url: action + "!" + metodo + ".action?idSeleccionado=" + idSeleccionado,
        load: function (type, data, evt) {

            //ocultarCampo(div, true);
            var div_modificado = dojo.byId(div);
            div_modificado.innerHTML = data;
            data.evalScripts();

            if (document.getElementById(indicador) != null) {
                ocultarCampo(indicador, true);
            }
        },
        mimetype: "text/plain"
    };
    dojo.io.bind(bindArgs);
}


function submitEdicionAjaxExtJS(action, metodo, campo, div) {
    document.getElementById("cargar").style.visibility = "visible";
    var idSeleccionado = document.getElementById(campo).value;
    var bindArgs = {
        url: action + "!" + metodo + ".action?idSeleccionado=" + idSeleccionado,
        load: function (type, data, evt) {
            var div_modificado = document.getElementById(div);
            div_modificado.innerHTML = data;
            data.evalScripts();
            modificar(action, div);//esta es la funcion que levanta el popup de extJS
            document.getElementById("cargar").style.visibility = "hidden";
            div_modificado.innerHTML = "";
        },
        mimetype: "text/plain"
    };
    dojo.io.bind(bindArgs);
}


function submitAjaxDosCampos(action, metodo, campo1, campo2, div, indicador) {
    var id1 = document.getElementById(campo1).value;
    var id2 = document.getElementById(campo2).value;

    if (document.getElementById(indicador) != null) {
        ocultarCampo(indicador, false);
    }
    var bindArgs = {
        url: action + "!" + metodo + ".action?idSeleccionado1=" + id1 + "&idSeleccionado2=" + id2,
        load: function (type, data, evt) {
            var div_modificado = document.getElementById(div);
            div_modificado.innerHTML = data;
            if (document.getElementById(indicador) != null) {
                ocultarCampo(indicador, true);
            }
        },
        mimetype: "text/plain"
    };
    dojo.io.bind(bindArgs);
}

function replaceLinksDisplayTag(idZona, idDisplayTable) {
    ajaxAnywhere.getZonesToReaload = function () {
        return idZona
    }
    ajaxAnywhere.onAfterResponseProcessing = function () {
        replaceLinks(idDisplayTable)
    }
    replaceLinks(idDisplayTable);
}

function replaceLinks(idDisplayTable) {
    // replace all the links in <thead> with onclick's that call AjaxAnywhere
    var sortLinks = $(idDisplayTable).getElementsByTagName('thead')[0]
        .getElementsByTagName('a');
    ajaxifyLinks(sortLinks);
    if (document.getElementsByClassName('pagelinks').length > 0) {
        var pagelinks = document.getElementsByClassName('pagelinks')[0]
            .getElementsByTagName('a');
        ajaxifyLinks(pagelinks);
    }
}

function ajaxifyLinks(links) {
    for (i = 0; i < links.length; i++) {
        links[i].onclick = function () {
            ajaxAnywhere.getAJAX(this.href);
            return false;
        }
    }
}

function submitAjax2(accion, div, formId, indicador) {


    if (indicador != "") {
        ocultarCampo(indicador, false);
    }
    var metodo = "";
    if (accion.search("!") != -1) {
        metodo = accion.substring(accion.search("!") + 1, accion.lastIndexOf("."));
    }
    if (metodo != "") {
        if (accion.indexOf("?") != -1) {
            accion += "&method:" + metodo;
        } else {
            accion += "?method:" + metodo;
        }
    }
    var bindArgs = {
        preventCache: true,
        url: accion,
        transport: "XMLHTTPTransport",
        method: "POST",
        mimetype: "text/html",
        load: function (type, data, evt) {

            var div_modificado;
            if (div != "") {
                div_modificado = document.getElementById(div);
            } else {
                div_modificado = document.getElementById(formId).parentNode.parentNode;
            }
            div_modificado.innerHTML = "";
            div_modificado.innerHTML = data;

            data.evalScripts();

            if (indicador != "") {
                ocultarCampo(indicador, true);
            }

        },
        error: function (type, error) {
            if (indicador != "") {
                ocultarCampo(indicador, true);
            }
            alert(error.message);
            //alert("Ha ocurrido un error en los datos proporcionados");
        },
        formNode: document.getElementById(formId)
    };
    dojo.io.bind(bindArgs);


}

function cargarComboAjax(idCombo, campo, action, metodo, td, indicador) {

    if (td != "") {
        var td_ind = document.getElementById(td);
        td_ind.style.display = "block";
    }
    if (indicador != "") {
        var ind = document.getElementById(indicador);
        ind.style.visibility = "visible";
    }

    Ext.Ajax.request({
        method: 'POST',
        url: action + '.action?method:' + metodo,
        params: {idSeleccionado: dojo.byId(campo).value},
        callback: function (options, success, response) {
            if (success) {
                Selectbox(response.responseXML, idCombo);
                if (td != "") {
                    var td_ind = document.getElementById(td);
                    td_ind.style.display = "none";
                }
                if (indicador != "") {
                    var ind = document.getElementById(indicador);
                    ind.style.visibility = "hidden";
                }
            }
        }
    });
}

function cargarMultiplesCombosAjax(listaCombos, campo, action, metodo, td, indicador) {

    if (td != "") {
        var td_ind = document.getElementById(td);
        td_ind.style.display = "block";
    }
    if (indicador != "") {
        var ind = document.getElementById(indicador);
        ind.style.visibility = "visible";
    }

    Ext.Ajax.request({
        method: 'POST',
        url: action + '.action?method:' + metodo,
        params: {idSeleccionado: dojo.byId(campo).value},
        callback: function (options, success, response) {
            if (success) {

                var list = response.responseText.split("~~split_SelBox~~");

                mySelectBox(list, listaCombos);

                if (td != "") {
                    var td_ind = document.getElementById(td);
                    td_ind.style.display = "none";
                }
                if (indicador != "") {
                    var ind = document.getElementById(indicador);
                    ind.style.visibility = "hidden";
                }

            }
        }
    });
}


function mySelectBox(responseText, resParam) {
    var list;
    var cadena = responseText;
    var targetNames = resParam.split(",");
    for (var k = targetNames.length - 1; k >= 0; k--) {
        var targetName = targetNames[k];
        var targetFields = document.getElementsByName(targetName);
        for (var j = 0; j < targetFields.length; j++) {
            var targetField = targetFields[j];
            targetField.options.length = 0;
            list = stringToDom(cadena[k]).getElementsByTagName("option");
            if (list != null) {
                targetField.disabled = false;
                for (var i = 0; i < list.length; i++) {
                    if (list[i].firstChild) {
                        var temp = list[i].firstChild.nodeValue;
                    }
                    targetField.options[i] = new Option(temp, list[i].getAttribute("value"));
                }
            }
        }
        try {
            targetFields[0].focus();
        } catch (error) {
        }
    }
}

function Selectbox(responseXML, resParam) {
    var list = responseXML.getElementsByTagName(
        "list").item(0).getElementsByTagName("option");
    var targetNames = resParam.split(",");
    for (var k = targetNames.length - 1; k >= 0; k--) {
        var targetName = targetNames[k];
        var targetFields = document.getElementsByName(targetName);
        for (var j = 0; j < targetFields.length; j++) {
            var targetField = targetFields[j];
            targetField.options.length = 0;
            if (list != null) {
                targetField.disabled = false;
                for (var i = 0; i < list.length; i++) {
                    if (list[i].firstChild) {
                        var temp = list[i].firstChild.nodeValue;
                    }
                    targetField.options[i] = new Option(temp, list[i].getAttribute("value"));
                }
            }
        }
        try {
            targetFields[0].focus();
        } catch (error) {
        }
    }
}

function stringToDom(string) {

    var xmlString = string;

    // code for IE
    if (window.ActiveXObject) {
        var dom = new ActiveXObject("Microsoft.XMLDOM");
        dom.loadXML(xmlString);
    }
    // code for Mozilla, Firefox, Opera, etc.
    else if (document.implementation && document.implementation.createDocument) {
        var dom = (new DOMParser()).parseFromString(xmlString, "text/xml");
    } else {
        alert('Your browser cannot handle this script');
    }

    var list = dom.childNodes[0];
    return list;
}


function verificarCamposFieldset() {
    var error = false;
    if (dojo.byId('min').value == "") {
        marcarCampoError('min', 'indicadorErrorMin', 'El campo M�nimo es requerido');
        error = true;
    }
    if (dojo.byId('max').value == "") {
        marcarCampoError('max', 'indicadorErrorMax', 'El campo M�ximo es requerido');
        error = true;
    }
    if (dojo.byId('almacen').value == "") {
        marcarCampoError('almacen', 'indicadorErrorAlmacen', 'El campo Almac�n es requerido');
        error = true;
    }
    return error;
}


function validarCamporLista(listaCampos, mensaje) {

    var i;
    var error = false;
    for (i = 0; i < listaCampos.length; i++) {
        if (document.getElementById(listaCampos[i]).value == "") {
            marcarCampoError(listaCampos[i], '', mensaje);
            error = true;
        }

    }

    return error;
}

/*function estilos(){
    modficarEstilo('min', 'w100p');
    modficarEstilo('max', 'w100p');
    modficarEstilo('almacen', 'w100p');
    document.getElementById('indicadorErrorMin').style.visibility = "hidden";
    document.getElementById('indicadorErrorMax').style.visibility = "hidden";
    document.getElementById('indicadorErrorAlmacen').style.visibility = "hidden";
}*/


function verificarExistencia(action, metodo, campoError, indicadorError, idCampoError) {

    if (id != "") {
        if (dojo.byId(campoError).value != "") {
            Ext.QuickTips.init();
            Ext.form.Field.prototype.msgTarget = 'side';
            Ext.Ajax.request({
                method: 'POST',
                url: action + '.action?method:' + metodo,
                params: {
                    idSeleccionado: dojo.byId(campoError).value,
                    id: dojo.byId(idCampoError).value
                },
                callback: function (options, success, response) {
                    if (success) {

                        if (response.responseText.search("true") != -1) {
                            document.getElementById(campoError).className = "x-form-invalid";
                            document.getElementById(indicadorError).style.visibility = "visible";

                            new Ext.ToolTip({
                                target: indicadorError,
                                cls: 'x-form-invalid-tip',
                                html: 'El elemento ya existe'
                            });

                            deshabilitarCampo('btnSalvar', true);
                        } else {
                            document.getElementById(campoError).className = "";
                            document.getElementById(indicadorError).style.visibility = "hidden";
                            deshabilitarCampo('btnSalvar', false);
                        }
                    }
                }
            });

        }
    }
}

function marcarCampoError(campo, icono, mensaje) {

    document.getElementById(campo).className = "x-form-invalid";
    if (icono != "") {
        document.getElementById(icono).style.visibility = "visible";
        tooltip(icono, mensaje);
    } else
        tooltip(campo, mensaje);


}

function existeError() {

}

function tooltip(campo, mensaje) {
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    new Ext.ToolTip({
        target: campo,
        cls: 'x-form-invalid-tip',
        html: mensaje
    });

}

function basicTooltip(campo, mensaje) {
    new Ext.ToolTip({
        target: campo,
        html: mensaje
    });
}

function modficarEstilo(campo, estilo) {
    document.getElementById(campo).className = estilo;

}

function soloDecimales_0_100(campo) {
    if (document.getElementById(campo).value < 0 || document.getElementById(campo).value > 100) {
        document.getElementById(campo).value = document.getElementById(campo).value.substr(0, 2);
        Ext.MessageBox.show({
            title: '<div style="text-align:left;">Error</div>',
            msg: 'S�lo decimales de 0 - 100.',
            buttons: Ext.MessageBox.OK,
            icon: Ext.MessageBox.ERROR
        });
    }
}

function verificarExistenciaSinDependencia(action, metodo, campoError, indicadorError, idCampoError) {


    if (dojo.byId(campoError).value != "") {

        Ext.QuickTips.init();
        Ext.form.Field.prototype.msgTarget = 'side';
        Ext.Ajax.request({
            method: 'POST',
            url: action + '.action?method:' + metodo,
            params: {
                idSeleccionado: dojo.byId(campoError).value,
                id: dojo.byId(idCampoError).value
            },
            callback: function (options, success, response) {
                if (success) {

                    if (response.responseText.search("true") != -1) {
                        document.getElementById(campoError).className = "x-form-invalid";
                        document.getElementById(indicadorError).style.visibility = "visible";

                        new Ext.ToolTip({
                            target: indicadorError,
                            cls: 'x-form-invalid-tip',
                            html: 'El elemento ya existe'
                        });
                    } else {
                        document.getElementById(campoError).className = "";
                        document.getElementById(indicadorError).style.visibility = "hidden";
                    }
                }
            }
        });

    }
}


/* FUNCIONES PARA M�DULOS ESPEC�FICOS */


/* FIN FUNCIONES PARA M�DULOS ESPEC�FICOS */
	


