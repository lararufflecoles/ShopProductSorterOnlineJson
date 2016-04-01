package es.rufflecol.lara.shopproductsorteronlinejson;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.rufflecol.lara.shopproductsorteronlinejson.model.Product;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String currencySymbol;
    private List<Product> products;
    private Resources resources;

    public RecyclerAdapter(String currencySymbol, List<Product> products, Resources resources) {
        this.currencySymbol = currencySymbol; /** Initialising the currencySymbol instance field **/
        this.products = products; /** As above **/
        this.resources = resources; /** As above **/
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        holder.productName.setText(product.getName());

        String formattedPrice = resources.getString(R.string.price, currencySymbol, decimalFormat.format(product.getPrice()));
        holder.productPrice.setText(formattedPrice);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView productPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            this.productName = (TextView) itemView.findViewById(R.id.product_name);
            this.productPrice = (TextView) itemView.findViewById(R.id.product_price);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public List<Product> getItems() {
        return products;
    }

    public void sortProductsAlphabeticallyAZ() {

        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                String lhsName = lhs.getName();
                String rhsName = rhs.getName();

                return lhsName.compareTo(rhsName);
            }
        });
        notifyDataSetChanged();
    }

    public void sortProductsAlphabeticallyZA() {

        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                String lhsName = lhs.getName();
                String rhsName = rhs.getName();

                return rhsName.compareTo(lhsName);
            }
        });
        notifyDataSetChanged();
    }

    public void sortProductsAscendingOrderPrice() {
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                double lhsPrice = lhs.getPrice();
                double rhsPrice = rhs.getPrice();

                if (lhsPrice == rhsPrice) {
                    return 0;
                } else if (lhsPrice > rhsPrice) {
                    return 1;
                } else { /** if (lhsPrice < rhsPrice) **/
                    return -1;
                }
            }
        });
        notifyDataSetChanged();
    }

    public void sortProductsDescendingOrderPrice() {
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                double lhsPrice = lhs.getPrice();
                double rhsPrice = rhs.getPrice();

                if (lhsPrice == rhsPrice) {
                    return 0;
                } else if (lhsPrice < rhsPrice) {
                    return 1;
                } else { /** if (lhsPrice > rhsPrice) **/
                    return -1;
                }
            }
        });
        notifyDataSetChanged();
    }
}