package com.arshadshah.nimaz.fragments.settings

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.arshadshah.nimaz.R

/**
 * The page where the terms and conditions are displayed
 * @author Arshad Shah
 * */
class TandC : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_tand_c, container, false)
        val fragmentName = requireArguments().getString("fragmentName")

        //change the action bar title to fragmentName
        activity?.title = fragmentName

        //first paragraph
        val TandC1: TextView = root.findViewById(R.id.TandC1)


        //email
        val email = getString(R.string.email)
        //html
        val TandCtext = "            <p>\n" +
                "                By downloading or using the app, these terms will\n" +
                "                automatically apply to you – you should make sure therefore\n" +
                "                that you read them carefully before using the app." +
                "                And you also shouldn’t try\n" +
                "                to translate the app into other languages, or make derivative\n" +
                "                versions. The app itself, and all the trade marks, copyright,\n" +
                "                database rights and other intellectual property rights related\n" +
                "                to it, still belong to <b>Arshad Shah</b>.\n" +
                "            </p>\n" +
                "            <p>\n" +
                "                <b>Arshad Shah</b> is committed to ensuring that the app is\n" +
                "                as useful and efficient as possible. For that reason, we\n" +
                "                reserve the right to make changes to the app or to charge for\n" +
                "                its services, at any time and for any reason. We will never\n" +
                "                charge you for the app or its services without making it very\n" +
                "                clear to you exactly what you’re paying for.\n" +
                "            </p>\n" +
                "            <p>\n" +
                "                The <b>Nimaz</b> app stores and processes personal data that\n" +
                "                you have provided to us, in order to provide my\n" +
                "                Service. It’s your responsibility to keep your phone and\n" +
                "                access to the app secure. We therefore recommend that you do\n" +
                "                not jailbreak or root your phone, which is the process of\n" +
                "                removing software restrictions and limitations imposed by the\n" +
                "                official operating system of your device. It could make your\n" +
                "                phone vulnerable to malware/viruses/malicious programs,\n" +
                "                compromise your phone’s security features and it could mean\n" +
                "                that the <b>Nimaz</b> app won’t work properly or at all.\n" +
                "            </p>\n" +
                "            <div>\n" +
                "                <p>\n" +
                "                    The app does use third party services that declare their own\n" +
                "                    Terms and Conditions.\n" +
                "                </p>\n" +
                "                <p>\n" +
                "                    Link to Terms and Conditions of third party service\n" +
                "                    providers used by the app\n" +
                "                </p>\n" +
                "                <ul>\n" +
                "                    <li><a href=\"https://policies.google.com/terms\" target=\"_blank\" rel=\"noopener noreferrer\">Google\n" +
                "                            Play\n" +
                "                            Services<br/>https://policies.google.com/terms</a></li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "            <p>\n" +
                "                You should be aware that there are certain things that\n" +
                "                <b>Arshad Shah</b> will not take responsibility for. Certain\n" +
                "                functions of the app will require the app to have an active\n" +
                "                internet connection. The connection can be Wi-Fi, or provided\n" +
                "                by your mobile network provider, but <b>Arshad Shah</b>\n" +
                "                cannot take responsibility for the app not working at full\n" +
                "                functionality if you don’t have access to Wi-Fi, and you don’t\n" +
                "                have any of your data allowance left.\n" +
                "            </p>\n" +
                "            <p></p>\n" +
                "            <p>\n" +
                "                If you’re using the app outside of an area with Wi-Fi, you\n" +
                "                should remember that your terms of the agreement with your\n" +
                "                mobile network provider will still apply. As a result, you may\n" +
                "                be charged by your mobile provider for the cost of data for\n" +
                "                the duration of the connection while accessing the app, or\n" +
                "                other third party charges. In using the app, you’re accepting\n" +
                "                responsibility for any such charges, including roaming data\n" +
                "                charges if you use the app outside of your home territory\n" +
                "                (i.e. region or country) without turning off data roaming. If\n" +
                "                you are not the bill payer for the device on which you’re\n" +
                "                using the app, please be aware that we assume that you have\n" +
                "                received permission from the bill payer for using the app.\n" +
                "            </p>\n" +
                "            <p>\n" +
                "                Along the same lines, <b>Arshad Shah</b> cannot always take\n" +
                "                responsibility for the way you use the app i.e. You need to\n" +
                "                make sure that your device stays charged – if it runs out of\n" +
                "                battery and you can’t turn it on to avail the Service,\n" +
                "                <b>Arshad Shah</b> cannot accept responsibility.\n" +
                "            </p>\n" +
                "            <p>\n" +
                "                With respect to <b>Arshad Shah's</b> responsibility for your\n" +
                "                use of the app, when you’re using the app, it’s important to\n" +
                "                bear in mind that although we endeavour to ensure that it is\n" +
                "                updated and correct at all times, we do rely on third parties\n" +
                "                to provide information to us so that we can make it available\n" +
                "                to you. <b>Arshad Shah</b> accepts no liability for any\n" +
                "                loss, direct or indirect, you experience as a result of\n" +
                "                relying wholly on this functionality of the app.\n" +
                "            </p>\n" +
                "            <p>\n" +
                "                At some point, we may wish to update the app. The app is\n" +
                "                currently available on Android – the requirements for\n" +
                "                system(and for any additional systems we\n" +
                "                decide to extend the availability of the app to) may change,\n" +
                "                and you’ll need to download the updates if you want to keep\n" +
                "                using the app. <b>Arshad Shah</b> does not promise that it\n" +
                "                will always update the app so that it is relevant to you\n" +
                "                and/or works with the Android version that you have\n" +
                "                installed on your device. However, you promise to always\n" +
                "                accept updates to the application when offered to you, We may\n" +
                "                also wish to stop providing the app, and may terminate use of\n" +
                "                it at any time without giving notice of termination to you.\n" +
                "                Unless we tell you otherwise, upon any termination, (a) the\n" +
                "                rights and licenses granted to you in these terms will end;\n" +
                "                (b) you must stop using the app, and (if needed) delete it\n" +
                "                from your device.\n" +
                "            </p>\n" +
                "            <p><strong>Changes to This Terms and Conditions</strong></p>\n" +
                "            <p>\n" +
                "                I may update our Terms and Conditions\n" +
                "                from time to time. Thus, you are advised to review this page\n" +
                "                periodically for any changes. I will\n" +
                "                notify you of any changes by posting the new Terms and\n" +
                "                Conditions on this page.\n" +
                "            </p>\n" +
                "            <p>\n" +
                "                These terms and conditions are effective as of 2021-05-17\n" +
                "            </p>\n" +
                "            <p><strong>Contact Us</strong></p>\n" +
                "            <p>\n" +
                "                If you have any questions or suggestions about my\n" +
                "                Terms and Conditions, do not hesitate to contact me\n" +
                "                at <br/>" +
                "$email \n" +
                "            </p>\n" +
                "            <p>This Terms and Conditions page was generated by <a\n" +
                "                    href=\"https://app-privacy-policy-generator.nisrulz.com/\" target=\"_blank\"\n" +
                "                    rel=\"noopener noreferrer\">https://app-privacy-policy-generator.nisrulz.com</a></p>"


        TandC1.text =
            Html.fromHtml(TandCtext, Html.FROM_HTML_MODE_LEGACY)

        return root
    }
}
