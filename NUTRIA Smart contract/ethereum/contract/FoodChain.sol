pragma solidity ^0.4.17;

contract FoodChain {
    address public manager;
    mapping(string => bool) productTagHashes;

    constructor() public {
        manager = msg.sender;
    }

    function addProductTagHash(string hash) public {
        productTagHashes[hash] = true;
    }

    function isHashValid(string hash) public view returns (bool) {
        return productTagHashes[hash];
    }
}