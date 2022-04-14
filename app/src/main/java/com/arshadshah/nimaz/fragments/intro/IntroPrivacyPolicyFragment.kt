package com.arshadshah.nimaz.fragments.intro

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.arshadshah.nimaz.R

class IntroPrivacyPolicyFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_intro_privacy_policy, container, false)


        val privacy1: TextView = root.findViewById(R.id.privacy1)

        val privacy1Text = "<p>\n" +
                "<h1>Privacy Policy</h1>" +
                "        <b>Arshad Shah</b> built the <b>Nimaz</b> app as\n" +
                "        a Free app. This SERVICE is provided by\n" +
                "        <b>Arshad Shah</b> at no cost and is intended for use as\n" +
                "        is.\n" +
                "    </p>\n" +
                "    <p>\n" +
                "        This page is used to inform visitors regarding my\n" +
                "        policies with the collection, use, and disclosure of Personal\n" +
                "        Information if anyone decided to use my Service.\n" +
                "    </p>\n" +
                "    <p>\n" +
                "        If you choose to use my Service, then you agree to\n" +
                "        the collection and use of information in relation to this\n" +
                "        policy. The Personal Information that I collect is\n" +
                "        used for providing and improving the Service. I will not use or share your information with\n" +
                "        anyone except as described in this Privacy Policy.\n" +
                "    </p>\n" +
                "    <p>\n" +
                "        The terms used in this Privacy Policy have the same meanings\n" +
                "        as in our Terms and Conditions, which is accessible at\n" +
                "        <b>Nimaz</b> unless otherwise defined in this Privacy Policy.\n" +
                "    </p>\n" +
                "    <p><strong>Information Collection and Use</strong></p>\n" +
                "    <p>\n" +
                "        For a better experience, while using our Service, I\n" +
                "        may require you to provide us with certain personally\n" +
                "        identifiable information. The information that\n" +
                "        I request will be retained on your device and is not collected by me in any way.\n" +
                "    </p>\n" +
                "    <div>\n" +
                "        <p>\n" +
                "            The app does use third party services that may collect\n" +
                "            information used to identify you.\n" +
                "        </p>\n" +
                "        <p>\n" +
                "            Link to privacy policy of third party service providers used\n" +
                "            by the app\n" +
                "        </p>\n" +
                "        <ul>\n" +
                "            <li><a href=\"https://www.google.com/policies/privacy/\" target=\"_blank\" rel=\"noopener noreferrer\">Google Play\n" +
                "                    Services <br/>https://www.google.com/policies/privacy</a></li>\n" +
                "\n" +
                "        </ul>\n" +
                "    </div>\n" +
                "    <p><strong>Log Data</strong></p>\n" +
                "    <p>\n" +
                "        I want to inform you that whenever you\n" +
                "        use my Service, in a case of an error in the app\n" +
                "        I collect data and information (through third party\n" +
                "        products) on your phone called Log Data. This Log Data may\n" +
                "        include information such as your device Internet Protocol\n" +
                "        (“IP”) address, device name, operating system version, the\n" +
                "        configuration of the app when utilizing my Service,\n" +
                "        the time and date of your use of the Service, and other\n" +
                "        statistics.\n" +
                "    </p>\n" +
                "    <p><strong>Cookies</strong></p>\n" +
                "    <p>\n" +
                "        Cookies are files with a small amount of data that are\n" +
                "        commonly used as anonymous unique identifiers. These are sent\n" +
                "        to your browser from the websites that you visit and are\n" +
                "        stored on your device's internal memory.\n" +
                "    </p>\n" +
                "    <p>\n" +
                "        This Service does not use these “cookies” explicitly. However,\n" +
                "        the app may use third party code and libraries that use\n" +
                "        “cookies” to collect information and improve their services.\n" +
                "        You have the option to either accept or refuse these cookies\n" +
                "        and know when a cookie is being sent to your device. If you\n" +
                "        choose to refuse our cookies, you may not be able to use some\n" +
                "        portions of this Service.\n" +
                "    </p>\n" +
                "    <p><strong>Service Providers</strong></p>\n" +
                "    <p>\n" +
                "        I may employ third-party companies and\n" +
                "        individuals due to the following reasons:\n" +
                "    </p>\n" +
                "    <ul>\n" +
                "        <li>To facilitate our Service;</li>\n" +
                "        <li>To provide the Service on our behalf;</li>\n" +
                "        <li>To perform Service-related services; or</li>\n" +
                "        <li>To assist us in analyzing how our Service is used.</li>\n" +
                "    </ul>\n" +
                "    <p>\n" +
                "        I want to inform users of this Service\n" +
                "        that these third parties have access to your Personal\n" +
                "        Information. The reason is to perform the tasks assigned to\n" +
                "        them on our behalf. However, they are obligated not to\n" +
                "        disclose or use the information for any other purpose.\n" +
                "    </p>\n" +
                "    <p><strong>Security</strong></p>\n" +
                "    <p>\n" +
                "        I value your trust in providing us your\n" +
                "        Personal Information, thus we are striving to use commercially\n" +
                "        acceptable means of protecting it. But remember that no method\n" +
                "        of transmission over the internet, or method of electronic\n" +
                "        storage is 100% secure and reliable, and I cannot\n" +
                "        guarantee its absolute security.\n" +
                "    </p>\n" +
                "    <p><strong>Links to Other Sites</strong></p>\n" +
                "    <p>\n" +
                "        This Service may contain links to other sites. If you click on\n" +
                "        a third-party link, you will be directed to that site. Note\n" +
                "        that these external sites are not operated by me.\n" +
                "        Therefore, I strongly advise you to review the\n" +
                "        Privacy Policy of these websites. I have\n" +
                "        no control over and assume no responsibility for the content,\n" +
                "        privacy policies, or practices of any third-party sites or\n" +
                "        services.\n" +
                "    </p>\n" +
                "    <p><strong>Children’s Privacy</strong></p>\n" +
                "    <p>\n" +
                "        These Services do not address anyone under the age of 13.\n" +
                "        I do not knowingly collect personally\n" +
                "        identifiable information from children under 13 years of age. In the case\n" +
                "        I discover that a child under 13 has provided\n" +
                "        me with personal information, I immediately\n" +
                "        delete this from our servers. If you are a parent or guardian\n" +
                "        and you are aware that your child has provided us with\n" +
                "        personal information, please contact me so that\n" +
                "        I will be able to do necessary actions.\n" +
                "    </p>\n" +
                "    <p><strong>Changes to This Privacy Policy</strong></p>\n" +
                "    <p>\n" +
                "        I may update our Privacy Policy from\n" +
                "        time to time. Thus, you are advised to review this page\n" +
                "        periodically for any changes. I will\n" +
                "        notify you of any changes by posting the new Privacy Policy on\n" +
                "        this page.\n" +
                "    </p>\n" +
                "    <p>This policy is effective as of 2021-05-17</p>\n" +
                "    <p><strong>Contact Us</strong></p>\n" +
                "    <p>\n" +
                "        If you have any questions or suggestions about my\n" +
                "        Privacy Policy, do not hesitate to contact me at Shaharshad57@gmail.com.\n" +
                "    </p>\n" +
                "    <p>This privacy policy page was created at <a href=\"https://privacypolicytemplate.net\" target=\"_blank\"\n" +
                "            rel=\"noopener noreferrer\">https://privacypolicytemplate.net </a> and modified/generated by <a\n" +
                "            href=\"https://app-privacy-policy-generator.nisrulz.com/\" target=\"_blank\" rel=\"noopener noreferrer\">App\n" +
                "            Privacy\n" +
                "            Policy Generator<br/>https://app-privacy-policy-generator.nisrulz.com</a></p>\n "




        privacy1.text =
            Html.fromHtml(privacy1Text, Html.FROM_HTML_MODE_LEGACY)

        val pandpcontinue: Button = root.findViewById(R.id.pandpcontinue)
        val pandpskip: Button = root.findViewById(R.id.pandpskip)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        pandpcontinue.setOnClickListener {
            val navcontroller = requireActivity().findNavController(R.id.fragmentContainerView)
            navcontroller.navigate(R.id.locationSelectionFragment)
            with(sharedPreferences.edit()) {
                putBoolean("isPrivacyPolicyApproved", true)
                apply()
            }
        }

        //if skipped
        pandpskip.setOnClickListener {
            val navcontroller = requireActivity().findNavController(R.id.fragmentContainerView)
            navcontroller.navigate(R.id.locationSelectionFragment)
            with(sharedPreferences.edit()) {
                putBoolean("isPrivacyPolicyApproved", false)
                apply()
            }
        }

        return root
    }
}