webpackJsonp([6],{1263:function(t,e,n){var s=n(1264);"string"==typeof s&&(s=[[t.i,s,""]]),s.locals&&(t.exports=s.locals);n(664)("7f1947ce",s,!0)},1264:function(t,e,n){e=t.exports=n(663)(!1),e.push([t.i,"",""])},1265:function(t,e,n){"use strict";var s=n(739),i=n(1266),a=n(817),o=n(37);e.a={data:function(){return{logoImg:o.a.LOGO,heightControl:{}}},mixins:[s.a,i.a,a.a],created:function(){var t=this;this.$service.personal.getPersonalInfoSilence().then(function(e){t.setUserInfo(e.data)}).catch(function(t){})},mounted:function(){var t=this;window.onresize=function(){t.setWindowMinHeight(window.innerHeight>600?window.innerHeight-t.ui.headerHeight:600),t.setWindowSideWidth(document.getElementById("sideBar").clientWidth-1)},window.onresize()},methods:{handleSelect:function(t){this.$router.push(t)},logout:function(){var t=this;this.$service.login.logout().then(function(){t.$router.push("/login")})}}}},1266:function(t,e,n){"use strict";var s=n(149),i=n.n(s),a=n(222);e.a={computed:i()({},Object(a.c)("menus",{menus:function(t){return t.menus}}))}},1267:function(t,e,n){"use strict";var s=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"oa-main"},[n("header",{staticClass:"oa-header"},[n("el-row",{staticClass:"container"},[n("el-col",{staticClass:"oa-logo",attrs:{span:4,id:"sideBar"}},[n("img",{attrs:{src:t.logoImg}})]),t._v(" "),n("el-col",{staticClass:"text-center",attrs:{span:1}},[n("h2",{staticClass:"font-normal space-m-l"},[t._v(" ")])]),t._v(" "),n("el-col",{attrs:{span:16}},[n("el-menu",{staticClass:"oa-menu",attrs:{"default-active":this.$route.path,theme:"dark",mode:"horizontal"},on:{select:t.handleSelect}},[t._l(t.menus,function(e){return[t.$lodash.isEmpty(e.subMenus)?[n("el-menu-item",{attrs:{index:e.index}},[t._v(t._s(e.name))])]:[n("el-submenu",{attrs:{index:e.index}},[n("template",{slot:"title"},[t._v(t._s(e.name))]),t._v(" "),t._l(e.subMenus,function(e){return[n("el-menu-item",{attrs:{index:e.index}},[t._v(t._s(e.name))])]})],2)]]})],2)],1),t._v(" "),n("el-col",{staticClass:"text-center",attrs:{span:3}},[n("a",{staticClass:"space-m-l",attrs:{href:"javascript:void(0)"},on:{click:t.logout}},[t._v("退出")])])],1)],1),t._v(" "),n("section",{staticClass:"container"},[n("router-view")],1)])},i=[],a={render:s,staticRenderFns:i};e.a=a},665:function(t,e,n){"use strict";function s(t){n(848)}Object.defineProperty(e,"__esModule",{value:!0});var i=n(850),a=n(851),o=n(68),r=s,c=o(i.a,a.a,!1,r,null,null);e.default=c.exports},734:function(t,e,n){"use strict";function s(t){n(1263)}Object.defineProperty(e,"__esModule",{value:!0});var i=n(1265),a=n(1267),o=n(68),r=s,c=o(i.a,a.a,!1,r,null,null);e.default=c.exports},739:function(t,e,n){"use strict";var s=n(101),i=n.n(s),a=n(149),o=n.n(a),r=n(222),c=n(104);e.a={computed:o()({},Object(r.c)("user",["userInfo","departments"])),watch:{userInfo:function(){this.$emit("$_userInfoOk")},departments:function(){this.$emit("$_departmentsOk")}},methods:o()({},Object(r.b)("user",{setUserInfo:c.a}),{$_getUserInfo:function(){var t=this;return new i.a(function(e){t.$lodash.isEmpty(t.userInfo)?t.$once("$_userInfoOk",function(){e(t.userInfo)}):e(t.userInfo)})},$_getDepartments:function(){var t=this;return new i.a(function(e){t.$lodash.isEmpty(t.departments)?t.$once("$_departmentsOk",function(){e(t.departments)}):e(t.departments)})}})}},760:function(t,e,n){"use strict";var s=n(149),i=n.n(s),a=n(222);e.a={computed:i()({},Object(a.c)("user",["authorityInfo"])),methods:{hasPermission:function(t){return this.authorityInfo.includes(t)}}}},817:function(t,e,n){"use strict";var s=n(149),i=n.n(s),a=n(222);e.a={computed:i()({},Object(a.c)("ui",["ui"])),methods:i()({},Object(a.b)("ui",{setWindowMinHeight:"setMinHeight",setWindowSideWidth:"setSideWidth"}))}},848:function(t,e,n){var s=n(849);"string"==typeof s&&(s=[[t.i,s,""]]),s.locals&&(t.exports=s.locals);n(664)("42325ecb",s,!0)},849:function(t,e,n){e=t.exports=n(663)(!1),e.push([t.i,"",""])},850:function(t,e,n){"use strict";var s=n(739),i=n(817),a=n(760);e.a={name:"Home",mixins:[s.a,i.a,a.a],data:function(){return{list:[{key:"goods.list",name:"商品管理",icon:"el-icon-leave",color:"#97cb3a",url:"/goods/list"},{key:"goods.category",name:"分类管理",icon:"el-icon-overtime",color:"#30beed",url:"/goods/categoryManage"},{key:"goods.spec",name:"规格管理",icon:"el-icon-out",color:"#0094d7",url:"/goods/specManage"},{key:"order.list",name:"订单管理",icon:"el-icon-business-trip",color:"#ff6a32",url:"/order/list"}],notices:[],approvings:[]}},created:function(){},computed:{heightControl:function(){return{height:this.ui.minHeight+"px"}},appList:function(){var t=this;return this.list.filter(function(e){return t.$lodash.isEmpty(e.key)||t.hasPermission(e.key)})}}}},851:function(t,e,n){"use strict";var s=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("el-row",[n("el-col",{staticClass:"oa-mainSide",style:t.heightControl,attrs:{span:4}},[n("dl",{staticClass:"oa-mainSide__userpic"},[n("dt",[n("img",{attrs:{src:t.$imgUrl(t.userInfo.icon)}})]),t._v(" "),n("dd",[t._v(t._s(t.userInfo.realName))])]),t._v(" "),n("hr",{staticClass:"line space-m-tb"}),t._v(" "),n("section",{staticClass:"oa-mainSide__user-info font-gray"},[n("p",[n("label",[t._v("职务:")]),t._v(" "),n("span",[t._v(t._s(t.userInfo.positionLevelStr))])]),t._v(" "),n("p",[n("label",[t._v("Q Q:")]),t._v(" "),n("span",[t._v(t._s(t.userInfo.qq))])]),t._v(" "),n("p",[n("label",[t._v("电话:")]),t._v(" "),n("span",[t._v(t._s(t.userInfo.mobile))])]),t._v(" "),n("p",[n("label",[t._v("邮箱:")]),t._v(" "),n("span",[t._v(t._s(t.userInfo.email))])])])]),t._v(" "),n("el-col",{staticClass:"oa-mainContent",style:t.heightControl,attrs:{span:20}},[n("div",{staticClass:"oa-appList-wrap"},[n("div",{staticClass:"oa-appList-title"},[t._v("\n        常用应用\n      ")]),t._v(" "),n("el-row",{staticClass:"oa-appList",attrs:{gutter:20}},[t._l(t.appList,function(e){return[n("el-col",{attrs:{span:3},nativeOn:{click:function(n){t.$router.push(e.url)}}},[n("dl",{staticClass:"oa-appList__item"},[n("dt",{style:{background:e.color}},[n("i",{class:e.icon})]),t._v(" "),n("dd",[t._v(t._s(e.name))])])])]})],2)],1),t._v(" "),n("hr",{staticClass:"line space-m-t"}),t._v(" "),n("section",{staticClass:"space-p"},[n("el-row",{attrs:{gutter:20}})],1)])],1)},i=[],a={render:s,staticRenderFns:i};e.a=a}});