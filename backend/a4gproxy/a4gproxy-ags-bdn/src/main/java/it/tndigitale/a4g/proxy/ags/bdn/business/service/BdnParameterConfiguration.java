package it.tndigitale.a4g.proxy.ags.bdn.business.service;

interface BdnParameterConfiguration {
	String USERNAME_KEY = "${it.tndigit.a4g.ags.bdn.username:appag_AGEA}"; // "bdn.username";

	String PASSWORD_KEY = "${it.tndigit.a4g.ags.bdn.password:t.org956}"; // "bdn.password";

	String URL_KEY = "${it.tndigit.a4g.ags.bdn.url:dummy_bdn_url}";
}
