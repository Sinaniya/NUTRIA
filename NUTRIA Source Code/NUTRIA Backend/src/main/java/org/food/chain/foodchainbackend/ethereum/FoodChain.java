package org.food.chain.foodchainbackend.ethereum;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.5.0.
 */
public class FoodChain extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5060008054600160a060020a03191633179055610287806100326000396000f3006080604052600436106100565763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663481c6a75811461005b57806359615d5414610099578063c5a54b7914610106575b600080fd5b34801561006757600080fd5b50610070610161565b6040805173ffffffffffffffffffffffffffffffffffffffff9092168252519081900360200190f35b3480156100a557600080fd5b506040805160206004803580820135601f81018490048402850184019095528484526100f294369492936024939284019190819084018382808284375094975061017d9650505050505050565b604080519115158252519081900360200190f35b34801561011257600080fd5b506040805160206004803580820135601f810184900484028501840190955284845261015f9436949293602493928401919081908401838280828437509497506101e89650505050505050565b005b60005473ffffffffffffffffffffffffffffffffffffffff1681565b60006001826040518082805190602001908083835b602083106101b15780518252601f199092019160209182019101610192565b51815160209384036101000a600019018019909216911617905292019485525060405193849003019092205460ff16949350505050565b600180826040518082805190602001908083835b6020831061021b5780518252601f1990920191602091820191016101fc565b51815160209384036101000a60001901801990921691161790529201948552506040519384900301909220805460ff1916931515939093179092555050505600a165627a7a72305820d2f8c1a86c3bf219c489ba91d1a999aec21512af1d70c417a7cb2ec4efb375550029";

    public static final String FUNC_MANAGER = "manager";

    public static final String FUNC_ISHASHVALID = "isHashValid";

    public static final String FUNC_ADDPRODUCTTAGHASH = "addProductTagHash";

    protected FoodChain(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected FoodChain(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> manager() {
        final Function function = new Function(FUNC_MANAGER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Boolean> isHashValid(String hash) {
        final Function function = new Function(FUNC_ISHASHVALID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> addProductTagHash(String hash) {
        final Function function = new Function(
                FUNC_ADDPRODUCTTAGHASH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(hash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<FoodChain> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(FoodChain.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<FoodChain> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(FoodChain.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static FoodChain load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new FoodChain(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static FoodChain load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new FoodChain(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
