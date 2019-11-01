package com.didu.lotteryshop.lotterya.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.0.
 */
public class LotteryAContract extends Contract {
    private static final String BINARY = "0x608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555030600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600281905550610af5806100a96000396000f3fe60806040526004361061007b5760003560e01c8063952bd3311161004e578063952bd331146100d65780639673bda414610127578063d6a9fe0f14610152578063d8da2cab1461017d5761007b565b80630cda84b3146100805780632492ca131461008a57806341c0e1b5146100b5578063478c4e0e146100cc575b600080fd5b6100886101d4565b005b34801561009657600080fd5b5061009f61025d565b6040518082815260200191505060405180910390f35b3480156100c157600080fd5b506100ca6102f6565b005b6100d4610368565b005b3480156100e257600080fd5b50610125600480360360208110156100f957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061044b565b005b34801561013357600080fd5b5061013c61073b565b6040518082815260200191505060405180910390f35b34801561015e57600080fd5b506101676107d5565b6040518082815260200191505060405180910390f35b34801561018957600080fd5b50610192610a46565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3373ffffffffffffffffffffffffffffffffffffffff167f555396cf4bf9a6047b5e55dc0059109ca296a00179309ed67f5c8fd21a7a17713460405180828152602001806020018281038252601c8152602001807f456e74727920696e746f20636f6e7472616374206163636f756e742e000000008152506020019250505060405180910390a2565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146102b857600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1631905090565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461034f57600080fd5b3373ffffffffffffffffffffffffffffffffffffffff16ff5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146103c157600080fd5b60006002819055503373ffffffffffffffffffffffffffffffffffffffff166108fc600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16319081150290604051600060405180830381858888f19350505050158015610448573d6000803e3d6000fd5b50565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16146104a457600080fd5b6000601e6064600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1631816104eb57fe5b040290507fdfd299acb82ae9cbb8aa8916c76356916ba69b9d9823b601a8942fb5e41d718d3382604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001806020018281038252601e8152602001807f4c6f747465727920706c6174666f726d2068616e646c696e67206665652e0000815250602001935050505060405180910390a13373ffffffffffffffffffffffffffffffffffffffff166108fc829081150290604051600060405180830381858888f193505050501580156105d9573d6000803e3d6000fd5b507fdfd299acb82ae9cbb8aa8916c76356916ba69b9d9823b601a8942fb5e41d718d82600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1631604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001806020018281038252601c8152602001807f54686520696e636f6d65206f662077696e6e696e67207072697a652e00000000815250602001935050505060405180910390a18173ffffffffffffffffffffffffffffffffffffffff166108fc600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16319081150290604051600060405180830381858888f19350505050158015610736573d6000803e3d6000fd5b505050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461079657600080fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1631905090565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461083057600080fd5b6002600081548092919060010191905055506000444260405160200180838152602001828152602001925050506040516020818303038152906040528051906020012060001c90506003818161088257fe5b0690507f9976c8a220f23ba4da79864742ae578c243e0fb719cab1881e03c35db3e249898160025460405180838152602001828152602001806020018281038252600d8152602001807f4c75636b79206e756d6265722e00000000000000000000000000000000000000815250602001935050505060405180910390a16000600a606480600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16318161094757fe5b048161094f57fe5b040290507fdfd299acb82ae9cbb8aa8916c76356916ba69b9d9823b601a8942fb5e41d718d3382604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200180602001828103825260118152602001807f4c6f74746572792064726177206665652e000000000000000000000000000000815250602001935050505060405180910390a13373ffffffffffffffffffffffffffffffffffffffff166108fc829081150290604051600060405180830381858888f19350505050158015610a3d573d6000803e3d6000fd5b50819250505090565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610aa157600080fd5b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690509056fea165627a7a72305820d465ea7af0e532563b6e88c60387ae67a0be1e18b3bef7d35d3c79da9d76b6690029";

    public static final String FUNC_SHOWMANAGEBALANCE = "ShowManageBalance";

    public static final String FUNC_SHOWCONTRACTMONEY = "ShowContractMoney";

    public static final String FUNC_SHOWCONTRACTADR = "ShowContractAdr";

    public static final String FUNC_CONTRACTBALANCEIN = "ContractBalanceIn";

    public static final String FUNC_GENLUCKNUM = "GenLuckNum";

    public static final String FUNC_RESETDATA = "resetData";

    public static final String FUNC_KILL = "kill";

    public static final Event CONTRACTBALANCEINEVENT_EVENT = new Event("ContractBalanceInEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event GENLUCKNUMEVENT_EVENT = new Event("GenLuckNumEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event CONTRACTBALANCEOUTEVENT_EVENT = new Event("ContractBalanceOutEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected LotteryAContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LotteryAContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LotteryAContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LotteryAContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ContractBalanceInEventEventResponse> getContractBalanceInEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(CONTRACTBALANCEINEVENT_EVENT, transactionReceipt);
        ArrayList<ContractBalanceInEventEventResponse> responses = new ArrayList<ContractBalanceInEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ContractBalanceInEventEventResponse typedResponse = new ContractBalanceInEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.buyer = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.money = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.msg = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ContractBalanceInEventEventResponse> contractBalanceInEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ContractBalanceInEventEventResponse>() {
            @Override
            public ContractBalanceInEventEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(CONTRACTBALANCEINEVENT_EVENT, log);
                ContractBalanceInEventEventResponse typedResponse = new ContractBalanceInEventEventResponse();
                typedResponse.log = log;
                typedResponse.buyer = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.money = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.msg = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ContractBalanceInEventEventResponse> contractBalanceInEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRACTBALANCEINEVENT_EVENT));
        return contractBalanceInEventEventFlowable(filter);
    }

    public List<GenLuckNumEventEventResponse> getGenLuckNumEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(GENLUCKNUMEVENT_EVENT, transactionReceipt);
        ArrayList<GenLuckNumEventEventResponse> responses = new ArrayList<GenLuckNumEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            GenLuckNumEventEventResponse typedResponse = new GenLuckNumEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.luckNum = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.periodNum = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.msg = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<GenLuckNumEventEventResponse> genLuckNumEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, GenLuckNumEventEventResponse>() {
            @Override
            public GenLuckNumEventEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(GENLUCKNUMEVENT_EVENT, log);
                GenLuckNumEventEventResponse typedResponse = new GenLuckNumEventEventResponse();
                typedResponse.log = log;
                typedResponse.luckNum = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.periodNum = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.msg = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<GenLuckNumEventEventResponse> genLuckNumEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GENLUCKNUMEVENT_EVENT));
        return genLuckNumEventEventFlowable(filter);
    }

    public List<ContractBalanceOutEventEventResponse> getContractBalanceOutEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(CONTRACTBALANCEOUTEVENT_EVENT, transactionReceipt);
        ArrayList<ContractBalanceOutEventEventResponse> responses = new ArrayList<ContractBalanceOutEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ContractBalanceOutEventEventResponse typedResponse = new ContractBalanceOutEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.money = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.msg = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ContractBalanceOutEventEventResponse> contractBalanceOutEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ContractBalanceOutEventEventResponse>() {
            @Override
            public ContractBalanceOutEventEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(CONTRACTBALANCEOUTEVENT_EVENT, log);
                ContractBalanceOutEventEventResponse typedResponse = new ContractBalanceOutEventEventResponse();
                typedResponse.log = log;
                typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.money = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.msg = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ContractBalanceOutEventEventResponse> contractBalanceOutEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRACTBALANCEOUTEVENT_EVENT));
        return contractBalanceOutEventEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> ShowManageBalance() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SHOWMANAGEBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> ShowContractMoney() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SHOWCONTRACTMONEY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> ShowContractAdr() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SHOWCONTRACTADR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> ContractBalanceIn(BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CONTRACTBALANCEIN, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> ContractBalanceIn(String luckAdr) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CONTRACTBALANCEIN, 
                Arrays.<Type>asList(new Address(luckAdr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> GenLuckNum() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GENLUCKNUM, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> resetData(BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RESETDATA, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> kill() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_KILL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static LotteryAContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LotteryAContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LotteryAContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LotteryAContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LotteryAContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new LotteryAContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LotteryAContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LotteryAContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<LotteryAContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(LotteryAContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<LotteryAContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(LotteryAContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<LotteryAContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(LotteryAContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<LotteryAContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(LotteryAContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class ContractBalanceInEventEventResponse extends BaseEventResponse {
        public String buyer;

        public BigInteger money;

        public String msg;
    }

    public static class GenLuckNumEventEventResponse extends BaseEventResponse {
        public BigInteger luckNum;

        public BigInteger periodNum;

        public String msg;
    }

    public static class ContractBalanceOutEventEventResponse extends BaseEventResponse {
        public String winner;

        public BigInteger money;

        public String msg;
    }
}
