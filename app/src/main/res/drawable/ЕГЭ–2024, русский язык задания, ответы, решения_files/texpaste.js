function autoreplacementTex(s) {
    let input = s

    function p1_remove_s(str, p1, offset, s) {
        return str.replace(/\s/g, "");
    }

    function p1_remove_tilda(str, p1, offset, s) {
        return str.replace('~', '');
    }

    if (s.includes('<math>')) {

    } else {
        input = s
            .replaceAll('\\medpagebreak', '')
            .replaceAll('\\midinsert', '')
            .replaceAll('\\smallpagebreak', '')
            .replaceAll('\\endinsert', '')
            .replaceAll('\\endcaption', '')
            .replaceAll(/\\flushpar\\item\{([0-9]{1,2}).\}/g, '$1.')
            .replaceAll(/\\parindent([0-9]{1,2})pt\\flushpar\\itemitem\{([А-я]{1})\)\}/g, '$2)')
            .replaceAll(/\\parindent([0-9]{1,2})pt\\flushpar\\itemitem\{\ ([А-я]{1})\)\}/g, '$2)')
            .replaceAll(/\\hskip(.*?)cm/g, '')
            .replaceAll(/\\vspace\{(.*?)cm\}/g, '')
            .replaceAll(/\\captionwidth\{(.*?)cm\}/g, '')
            .replaceAll('\\flushpar', '')
            .replaceAll(/\{\\bf\ (.*?)\}/g, '##<b>$1</b>')


            .replaceAll(/(.{1})\n(.{1})/g, '$1 $2')
            .replaceAll(/(.{1})\r\n(.{1})/g, '$1 $2')

            .replaceAll(/\$([A-Za-z]\s[A-Za-z].{0,4}[\.\,\;\:]{0,1})\$/g, p1_remove_s)
            .replaceAll(/\$([A-Za-z]{1,5})([\.\,\;\:])\$/g, '<i>$1</i>$2')
            .replaceAll(/\$([A-Za-z]{1,5})\$/g, '<i>$1</i>')
            .replaceAll(/\$-([0-9]{1,10})\$/g, '−$1')
            .replaceAll(/\$([0-9]{1,10})\$/g, '$1')


            .replaceAll(/\$\$(.*?)\$\$/gms, '\n##<center>\n<math>$1</math>\n##</center>\n')
            .replaceAll(/\$(.+?)\$/g, '<math>$1</math>')
            .replaceAll(/\<\/math\>(\.|,|;|:|\?)/g, '$1</math>')


            .replaceAll(/\\special\{em:\ graph\ (.*?)\.pcx\}/g, '<img src="/mccme/$1.png" style="margin:10px auto;display:block;max-width:100%;">\n')
            .replaceAll(/\\special\{em:graph\ (.*?)\.pcx\}/g, '<img src="/mccme/$1.png" style="margin:10px auto;display:block;max-width:100%;">\n')
            .replaceAll(/\\botcaption\{\\fig\ (.*?)\}/g, '<!-- $1 -->')


            .replaceAll('ис.~', 'ис. ')
            .replaceAll('см.~', 'см. ')
            .replaceAll('{\\it Ответ:}', '<span style="letter-spacing: 2px;">Ответ:</span>')
            .replaceAll('{<b>', '<b>')

            .replaceAll(/([А-я]{1})\\\.\ ([А-я]{1})\\\./g, '<nobr>$1.&ensp;$2.</nobr>')

            .replaceAll(' ---', '&nbsp;—')
            .replaceAll('\r\n\r\n', '\n')
            .replaceAll('\r\n\r\n', '\n')
            .replaceAll('\n\n', '\n')

            .replaceAll('##<b>', '\n##<b>')


            .replaceAll(/\\al([^A-Za-z]{1})/g, '\\alpha$1')
            .replaceAll('\\alphapha', '\\alpha')
            .replaceAll('\<i\>\\alpha\<\/i\>', '&alpha;')
            .replaceAll(/\\be([^A-Za-z]{1})/g, '\\beta$1')
            .replaceAll('\\betata', '\\beta')
            .replaceAll('\<i\>\\beta\<\/i\>', '&beta;')
            .replaceAll(/\\ga([^A-Za-z]{1})/g, '\\gamma$1')
            .replaceAll('\\gammamma', '\\gamma')
            .replaceAll('\<i\>\\gamma\<\/i\>', '&gamma;')

            .replaceAll('\\num', '{\\rm num}')
            .replaceAll('\\const', '{\\rm const}')

            .replaceAll(/(\\[l|g]e)([^q^f]{1})/g, '$1qslant$2')
            .replaceAll(/(\\[l|g]eq)([^s]{1})/g, '$1slant$2')
            .replaceAll('\\leqslantqslant', '\\leqslant')
            .replaceAll('\\leqslantft', '\\left')
            .replaceAll('\\geqslantqslant', '\\geqslant')

            .replaceAll('\\vp', '\\varphi')
            .replaceAll('\\equd', '\\stackrel{\\rm de f}{=}')


            .replaceAll(' ##', '##')
            .replaceAll('{##', '##')
            .replaceAll('{\n##', '##')

            .replaceAll(/\\\[([A-Za-z\s]{2,10})[\.\,\;\:]{0,1}\\\]/g, p1_remove_s)
            .replaceAll(/\\\[([A-Za-z]{1,5})([\.\,\;\:])\\\]/g, '<i>$1</i>$2')
            .replaceAll(/\\\[([A-Za-z]{1,5})\\\]/g, '<i>$1</i>')
            .replaceAll(/\\\[-([0-9]{1,10})\\\]/g, '−$1')
            .replaceAll(/\\\[([0-9]{1,10})\\\]/g, '$1')
            .replaceAll(/\\\[(.+?)\\\]/g, '<math>$1</math>')
            .replaceAll(/\<\/math\>(\.|,|;|:|\?)/g, '$1</math>')

            .replaceAll(/\ ([А-я]{1})\)/g, '\n$1)')
            .replaceAll('\\operatorname{\\Im}', '\\text {Im} \\, ')
            .replaceAll('\\operatorname{\\Re}', '\\text {Re} \\, ')
            .replaceAll('\\operatorname{Im}', '\\text {Im} \\, ')
            .replaceAll('\\operatorname{Re}', '\\text {Re} \\, ')

            .replaceAll(/\\operatorname[\s]{0,2}\{([A-Za-z\s]{1,14})\}/g, p1_remove_s)
            .replaceAll(/\\mathrm[\s]{0,2}\{([A-Za-z\s~]{1,14})\}/g, p1_remove_s)
            .replaceAll(/\\mathrm[\s]{0,2}\{([A-Za-z\s~]{1,14})\}/g, p1_remove_tilda)
            .replaceAll('\\operatorname{const}', '\\text {const} ')
            .replaceAll(/\\operatorname\{([A-Za-z]{1,14})\}/g, '\\$1 ')
            .replaceAll(/\\mathrm\{([A-Za-z]{1,6})\}/g, ' $1 ')

            .replaceAll(/\\in[\s]([ZNR])/g, '\\in \\mathbb{$1}')
            .replaceAll('  ', ' ')
            .replaceAll(' _', '_')
            .replaceAll(' ', ' ')
    }

    return input;
}
