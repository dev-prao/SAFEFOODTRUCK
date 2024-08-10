import React, { useEffect, useState } from 'react';
import Cookies from 'js-cookie';
import styles from './CustomerCart.module.css';
import { useNavigate , useLocation } from 'react-router-dom';
import axiosInstance from 'utils/axiosInstance';
import customerOrderStore from 'store/orders/customerOrderStore';

const CustomerCart = () => {
  const [cartItems, setCartItems] = useState([]);
  const [request, setRequest] = useState('');
  const navigate = useNavigate();

  const location = useLocation();

  const { storeId } = location.state;

  const { setNowOrderId , nowOrderId } = customerOrderStore();

  const nickname = sessionStorage.getItem('nickname');

  useEffect(() => {
    const cart = Cookies.get('cart');
    if (cart) {
      setCartItems(JSON.parse(cart));
    }
  }, []);

  const updateCartInCookies = (updatedItems) => {
    setCartItems(updatedItems);
    Cookies.set('cart', JSON.stringify(updatedItems), { expires: 1 / 72 });
  };

  const handleIncrease = (index) => {
    const updatedItems = [...cartItems];
    updatedItems[index].quantity += 1;
    updateCartInCookies(updatedItems);
  };

  const handleDecrease = (index) => {
    const updatedItems = [...cartItems];
    if (updatedItems[index].quantity > 1) {
      updatedItems[index].quantity -= 1;
    } else {
      updatedItems.splice(index, 1);
    }
    updateCartInCookies(updatedItems);
  };

  const handleCheckout = async () => {
    const payload = {
      storeId: storeId,
      request: "",
      menuList: cartItems.map(item => ({
        menuId: item.menuId,
        count: item.quantity
      }))
    };
    
    try {
     const response =  await axiosInstance.post('orders', payload);
      alert('주문이 완료되었습니다!');

      const nowOrder = response.data

      console.log(nowOrder)

      setNowOrderId(nowOrder.orderId)

      console.log(nowOrderId)

      Cookies.remove('cart');

      navigate('/mypageCustomer');

    } catch (error) {

      console.error('결제 실패:', error);

      alert('결제에 실패했습니다.');

    }
  };

  const totalAmount = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);

  return (
    <div className={styles.cartContainer}>
      <h1>{nickname} 님의 결제예정 금액이에요!</h1>
      {cartItems.length > 0 ? (
        <>
          {cartItems.map((item, index) => (
            <div key={item.menuId} className={styles.cartItem}>
              <img src={item.menuImageDto.savedUrl} alt={item.name} className={styles.image} />
              <div className={styles.details}>
                <h3>{item.name}</h3>
                <p>{item.price}원</p>
              </div>
              <div className={styles.quantityControl}>
                <button onClick={() => handleDecrease(index)}>-</button>
                <span>{item.quantity}</span>
                <button onClick={() => handleIncrease(index)}>+</button>
              </div>
            </div>
          ))}
          <div className={styles.requestBox}>
            <label htmlFor="request">요구사항:</label>
            <input
              type="text"
              id="request"
              value={request}
              onChange={(e) => setRequest(e.target.value)}
              className={styles.requestInput}
            />
          </div>
          <button className={styles.checkoutButton} onClick={handleCheckout}>
            {totalAmount}원 주문 할게요!
          </button>
        </>
      ) : (
        <div className={styles.emptyCartMessage}>
          <p>장바구니에 담긴 상품이 없어요 🤣</p>
        </div>
      )}
    </div>
  );
};

export default CustomerCart;
