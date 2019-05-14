import Vue from 'vue'
import 'element-ui/lib/theme-chalk/index.css'
// import 'element-ui/lib/theme-chalk/display.css'
// import 'element-ui/lib/theme-chalk/base.css'

import {
  Pagination,
  // Dialog,
  Menu,
  Submenu,
  MenuItem,
  Form,
  FormItem,
  Input,
  Radio,
  RadioGroup,
  RadioButton,
  InputNumber,
  Select,
  Option,
  OptionGroup,
  Button,
  Table,
  TableColumn,
  DatePicker,
  Popover,
  Tooltip,
  Card,
  // Badge,
  Container,
  Header,
  Aside,
  Main,
  Row,
  Col,
  MessageBox,
  Message,
  Loading,
  Tree,
  Dialog,
} from 'element-ui'

Vue.use(Pagination);
// Vue.use(Dialog);
Vue.use(Menu);
Vue.use(Submenu);
Vue.use(MenuItem);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Input);
Vue.use(InputNumber);
Vue.use(Radio);
Vue.use(RadioGroup);
Vue.use(RadioButton);
Vue.use(Select);
Vue.use(Option);
Vue.use(OptionGroup);
Vue.use(Button);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(DatePicker);
Vue.use(Tooltip);
Vue.use(Popover);
Vue.use(Card);
// Vue.use(Badge);
Vue.use(Container);
Vue.use(Header);
Vue.use(Aside);
Vue.use(Main);
Vue.use(Row);
Vue.use(Col);
Vue.use(Tree);
Vue.use(Dialog);

Vue.use(Loading.directive);

Vue.prototype.$loading = Loading.service;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$message = Message;
