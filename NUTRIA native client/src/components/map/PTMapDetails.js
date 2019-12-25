// import React, {Component} from 'react';
// import {Modal, Text, TouchableHighlight, View, Alert} from 'react-native';
// import {connect} from "react-redux";
// import {closePtMapViewModal} from "../../store/actions/uiActionCreators";
// import MapScreen from "../../screens/map/MapScreen";
// import Layout from "../../constants/Layout";
//
//
//
// class PTMapDetails extends Component {
//     render() {
//         return (
//             <View style={{marginTop: 22}}>
//                 <Modal
//                     animationType="slide"
//                     transparent={false}
//                     visible={this.props.isMapModalOpen}
//                     onRequestClose={() => {
//                         Alert.alert('Modal has been closed.');
//                     }}>
//                     <View style={{marginTop: 22}}>
//                         <View>
//                             <View style={{
//                                 height: Layout.window.height * 0.9,
//                                 width: Layout.window.width
//                             }}>
//                                 <MapScreen/>
//                             </View>
//
//                             <TouchableHighlight
//                                 onPress={() => {
//                                     this.props.onModalClose()
//                                 }}>
//                                 <Text>Hide Modal</Text>
//                             </TouchableHighlight>
//                         </View>
//                     </View>
//                 </Modal>
//
//                 {/*<TouchableHighlight*/}
//                     {/*onPress={() => {*/}
//                         {/*this.setModalVisible(true);*/}
//                     {/*}}>*/}
//                     {/*<Text>Show Modal</Text>*/}
//                 {/*</TouchableHighlight>*/}
//             </View>
//         );
//     }
// }
//
// const mapDispatchToProps = dispatch => {
//     return {
//         onModalClose: () => dispatch(closePtMapViewModal())
//     }
// };
//
// const mapStateToProps = state => {
//     return {
//         isMapModalOpen: state.ui.isMapModalOpen
//     };
// };
//
// export default connect(mapStateToProps, mapDispatchToProps)(PTMapDetails);