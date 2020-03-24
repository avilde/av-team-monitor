import Vue from 'vue';
import App from './App';
import TM from './tm';
import Vuetify from 'vuetify';
import VueCookie from 'vue-cookie';

import 'vuetify/dist/vuetify.min.css';
import {library} from '@fortawesome/fontawesome-svg-core';
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome';

import {
	faCoffee,
	faBug,
	faCog,
	faVial,
	faBookmark,
	faThumbtack,
	faSearch,
	faBook,
	faQuestionCircle,
	faFilter,
	faPlusCircle,
	faFileWord,
	faFileExcel,
	faDownload,
	faTimesCircle,
	faInfoCircle,
	faAngleUp,
	faAngleDoubleUp
} from '@fortawesome/free-solid-svg-icons';

library.add([
	faCoffee,
	faBug,
	faCog,
	faVial,
	faBookmark,
	faThumbtack,
	faSearch,
	faBook,
	faQuestionCircle,
	faFilter,
	faPlusCircle,
	faFileWord,
	faFileExcel,
	faDownload,
	faTimesCircle,
	faInfoCircle,
	faAngleUp,
	faAngleDoubleUp
]);
Vue.component('fa-icon', FontAwesomeIcon);

Vue.use(Vuetify);
Vue.use(VueCookie);

export const bus = new Vue();

Vue.config.productionTip = false;

/* eslint-disable no-new */
function initApp() {
	new Vue({
		el: '#tm-app',
		render: h => h(App)
	});
}

window.TM = TM;

bus.$on('tmLoaded', initApp);
bus.$on('errorMsg', msg => {
	document.getElementById('error-msg').innerHTML = msg;
});
