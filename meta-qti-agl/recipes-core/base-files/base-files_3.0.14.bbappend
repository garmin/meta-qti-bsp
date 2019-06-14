do_update_smack_rule() {
      # replace the old smack rules from "System _ -----l" to "System _ rwxa--"
      sed -i "1c System _ rwxa--"  ${D}/${sysconfdir}/smack/accesses.d/default-access-domains
}

addtask do_update_smack_rule after do_install before do_package
